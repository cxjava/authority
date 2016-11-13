package com.chenxin.authority.service;

import com.chenxin.authority.dao.BaseUserRepository;
import com.chenxin.authority.dao.BaseUserRoleRepository;
import com.chenxin.authority.entity.BaseUser;
import com.chenxin.authority.entity.BaseUserRole;
import com.chenxin.authority.entity.ExtPager;
import com.chenxin.authority.util.EncryptUtil;
import com.chenxin.authority.util.JpaTools;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

@Service
@Transactional
public class BaseUserService {
    @Autowired
    private BaseUserRepository baseUserRepository;
    @Autowired
    private BaseUserRoleRepository baseUserRoleRepository;
    /**
     * 读取配置文件的值，分号后面为没有此配置项时的默认值
     */
    @Value("${email.host:smtp.163.com}")
    private String emailHost;
    @Value("${email.account:test@qq.com.cn}")
    private String emailAccount;
    @Value("${email.password:test}")
    private String emailPassword;
    @Value("${system.url:http://localhost:8888/}")
    private String systemUrl;
    /**
     * 重置的密码
     */
    @Value("${reset.password:e10adc3949ba59abbe56e057f20f883e}")
    private String resetPassword;
    /**
     * 限制时间
     */
    @Value("${limit.millis:3600000}")
    private Long millis;
    /**
     * 提示时间
     */
    @Value("${limit.millis.text:一小时}")
    private String millisText;

    private static final Logger logger = LoggerFactory.getLogger(BaseUserService.class);

    public String selectByAccount(Map<String, Object> parameters) {
        String account = MapUtils.getString(parameters, "account");
        // 条件查询
        List<BaseUser> list = this.baseUserRepository.findByAccount(account);
        if (CollectionUtils.isEmpty(list)) {
            // 没有此用户名
            return "00";
        }
        BaseUser dataBaseUser = list.get(0);
        logger.info("dataBaseUser:{}", dataBaseUser);
        // 错误3次,并且时间未到
        if (dataBaseUser.getErrorCount() >= 3 && compareTo(dataBaseUser.getLastLoginTime())) {
            return "请你联系管理员，或者" + millisText + "分钟之后再次尝试！";
        }
        // 传入的password已经md5过一次了,并且为小写
        if (!EncryptUtil.match(MapUtils.getString(parameters, "passwordIn") + account, dataBaseUser.getPassword())) {
            // 密码不正确
            return loginTimes(dataBaseUser, MapUtils.getString(parameters, "loginIp"));
        }
        // 更新最后登录时间和登录ip
        dataBaseUser.setErrorCount(0);
        dataBaseUser.setLastLoginTime(new Date());
        dataBaseUser.setLastLoginIp(MapUtils.getString(parameters, "loginIp"));
        this.baseUserRepository.save(dataBaseUser);
        // controller中取出放到session中
        parameters.put("baseUser", dataBaseUser);
        return "01";
    }

    @Transactional
    public void deleteByPrimaryKey(Long id) {
        if (1L == id) {
            throw new ServiceException("不能删除超级管理员！", "007");
        }
        // 删除角色中的
        this.baseUserRoleRepository.deleteByUserId(id);
        this.baseUserRepository.delete(id);
    }

    @Transactional
    public void updateUserPassword(Map<String, Object> parameters) {
        Long userId = MapUtils.getLong(parameters, "userId");
        String newPassword = MapUtils.getString(parameters, "newPassword");
        // 保存新密码
        BaseUser baseUsers = this.baseUserRepository.findOne(userId);
        baseUsers.setPassword(EncryptUtil.encrypt(newPassword + baseUsers.getAccount()));
        this.baseUserRepository.save(baseUsers);
    }

    @Transactional
    public String saveUser(BaseUser user, Collection<Long> roleIds) {
        // 判断用户名是否重复
        if (null == user.getId()) {
            // TODO: 如果是已经存在的用户，并且用户要修改用户名，则需要判断
            List<BaseUser> list = this.baseUserRepository.findByAccount(user.getAccount());
            if (!CollectionUtils.isEmpty(list)) {
                return "帐号已经被注册！请重新填写!";
            }
            // 新建的用户,加密保存下
            user.setPassword(EncryptUtil.encrypt(user.getPassword() + user.getAccount()));
            user.setLastLoginTime(new Date());
            user.setErrorCount(0);
        } else {
            BaseUser target = this.baseUserRepository.findOne(user.getId());
            user.setPassword(EncryptUtil.encrypt(target.getPassword() + target.getAccount()));
            user.setLastLoginTime(target.getLastLoginTime());
        }
        this.baseUserRepository.save(user);
        // 更新用户的角色信息
        // 删除已有的用户角色信息
        this.baseUserRoleRepository.deleteByUserId(user.getId());
        // 保存新的角色信息
        for (Long roleId : roleIds) {
            BaseUserRole role = new BaseUserRole();
            role.setRoleId(roleId);
            role.setUserId(user.getId());
            this.baseUserRoleRepository.save(role);
        }
        return "01";
    }

    @Transactional
    public String resetPwdByPrimaryKey(Long userId) {
        BaseUser oldUser = this.baseUserRepository.findOne(userId);
        if (oldUser == null) {
            return "没有找到该用户！";
        }
        oldUser.setErrorCount(0);// 重置下登录错误次数
        oldUser.setPassword(EncryptUtil.encrypt(resetPassword + oldUser.getAccount()));
        return this.baseUserRepository.save(oldUser) != null ? "01" : "00";
    }


    @Transactional
    public void update(BaseUser user) {
        BaseUser target = this.baseUserRepository.findOne(user.getId());
        target.setRealName(user.getRealName());
        target.setSex(user.getSex());
        target.setEmail(user.getEmail());
        target.setMobile(user.getMobile());
        target.setOfficePhone(user.getOfficePhone());
        target.setRemark(user.getRemark());
        this.baseUserRepository.save(target);
    }


    public boolean validateAccount(String account) {
        List<BaseUser> list = this.baseUserRepository.findByAccount(account);
        if (!CollectionUtils.isEmpty(list)) {
            return false;
        }
        return true;
    }

    /**
     * 限制密码输入次数
     *
     * @param baseUsers
     * @param loginIp
     * @return
     */
    private String loginTimes(BaseUser baseUsers, String loginIp) {
        String info = "";
        Integer errorCount = baseUsers.getErrorCount();
        Date lastLoginTime = baseUsers.getLastLoginTime();
        if (!compareTo(lastLoginTime)) {// 已经过了XX分钟，那么把errorCount设置为0，重新计数。
            errorCount = 0;
        }
        switch (errorCount) {
            case 0:
                // 第一次输入密码错误
                info = "密码错误!你还有2次机会输入密码!";
                errorCount = errorCount + 1;
                break;
            case 1:
                // 第二次输入密码错误
                info = "密码错误!你还有1次机会输入密码!<br/>如果输入错误，帐户将被锁定不能登录！";
                errorCount = errorCount + 1;
                break;
            case 2:
                // 第三次输入密码错误
                info = "密码错误!你的帐户已经被锁定！<br/>请联系管理员！";
                errorCount = errorCount + 1;
                break;
            default:
                break;
        }
        // 保存新的错误次数和时间
        baseUsers.setErrorCount(errorCount);
        baseUsers.setLastLoginTime(new Date());
        baseUsers.setLastLoginIp(loginIp);
        this.baseUserRepository.save(baseUsers);
        return info;
    }

    /**
     * 日期比较
     *
     * @param date
     * @return
     */
    private boolean compareTo(Date date) {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.setTime(date);
        long lastly = c.getTimeInMillis();
        return (now - lastly) <= millis;
    }


    @Transactional
    public String findPassword(BaseUser user) {
        // 查询是否存在
        List<BaseUser> list = this.baseUserRepository.findByAccountAndEmail(user.getAccount(), user.getEmail());
        if (CollectionUtils.isEmpty(list)) {
            return "请输入正确的帐号和其注册邮箱！";
        }
        BaseUser dataBaseUser = list.get(0);
        // 设置token
        String token = EncryptUtil.encrypt(RandomStringUtils.randomAlphanumeric(10));

        dataBaseUser.setLastLoginTime(new Date());// 记录发送连接时间
        dataBaseUser.setPassword(token);// 设置密码
        this.baseUserRepository.save(dataBaseUser);

        String title = "亲爱的 " + dataBaseUser.getAccount() + "，请重新设置你的帐户密码！";
        String url = systemUrl + token.toUpperCase() + "/" + dataBaseUser.getId();
        url = "<a href='" + url + "'/>" + url + "</a>";
        // 一小时有效
        String body = "请点击下面链接，重新设置您的密码：<br/>" + url + " ,此链接一小时有效!<br/>" + "如果该链接无法点击，请直接拷贝以上网址到浏览器地址栏中访问。";
        this.execSend(dataBaseUser.getEmail(), title, body);
        return "01";
    }

    /**
     * 发送邮件
     */
    private boolean execSend(String address, String title, String body) {
        Properties props = new Properties();
        // 定义邮件服务器的地址
        props.put("mail.smtp.host", emailHost);
        props.put("mail.smtp.auth", "true");
        // 取得Session
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        // 邮件标题
        try {
            message.setSubject(title);
            // 发件人的邮件地址
            message.setFrom(new InternetAddress(emailAccount));
            // 接收邮件的地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            // 邮件发送的时间日期
            message.setSentDate(new Date());
            // 新建一个MimeMultipart对象用来存放BodyPart对象 related意味着可以发送html格式的邮件
            Multipart mp = new MimeMultipart("related");
            // 新建一个存放信件内容的BodyPart对象
            BodyPart bodyPart = new MimeBodyPart();// 正文
            // 给BodyPart对象设置内容和格式/编码方式
            bodyPart.setContent(body, "text/html;charset=utf-8");
            // 将BodyPart加入到MimeMultipart对象中
            mp.addBodyPart(bodyPart);
            // 设置邮件内容
            message.setContent(mp);
            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("MessagingException:{}", e);
            throw new ServiceException(e, "邮件发送失败！", "");
        }
        logger.info("向邮件地址:{}发送邮件成功！", address);
        return true;
    }


    public Page<BaseUser> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
        PageRequest pageable = JpaTools.getPageRequest(pager, "");
        Specification<BaseUser> spec = JpaTools.getSpecification(parameters, BaseUser.class);
        return this.baseUserRepository.findAll(spec, pageable);
    }


    public String updatePassword(Map<String, Object> parameters) {
        Long id = MapUtils.getLong(parameters, "userId");
        String password = MapUtils.getString(parameters, "password");
        BaseUser user = this.baseUserRepository.findOne(id);
        if (user == null) {
            return "00";
        }
        user.setPassword(EncryptUtil.encrypt(password + user.getAccount()));
        this.baseUserRepository.save(user);
        return "01";
    }


    public List<BaseUserRole> selectRolesByUserId(Long userId) {
        return this.baseUserRoleRepository.findByUserId(userId);
    }


    public BaseUser findOne(Long userId) {
        return this.baseUserRepository.findOne(userId);
    }

}