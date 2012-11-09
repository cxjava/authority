package com.chenxin.authority.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

import com.chenxin.authority.common.utils.EncryptUtil;
import com.chenxin.authority.common.utils.JpaTools;
import com.chenxin.authority.dao.BaseUserRepository;
import com.chenxin.authority.dao.BaseUserRoleRepository;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.service.BaseUserService;
import com.chenxin.authority.service.ServiceException;
@Service
@Transactional
public class BaseUserServiceImpl implements BaseUserService {
	@Autowired
	private BaseUserRepository baseUserRepository;
	@Autowired
	private BaseUserRoleRepository baseUserRoleRepository;
	/** 读取配置文件的值，分号后面为没有此配置项时的默认值 */
	@Value("${email.host:smtp.163.com}")
	private String emailHost;
	@Value("${email.account:test@whty.com.cn}")
	private String emailAccount;
	@Value("${email.password:test}")
	private String emailPassword;
	@Value("${system.url:http://localhost:8888/}")
	private String systemUrl;
	/** 重置的密码 */
	@Value("${reset.password:e10adc3949ba59abbe56e057f20f883e}")
	private String resetPassword;
	/** 限制时间 */
	@Value("${limit.millis:3600000}")
	private Long millis;
	/** 提示时间 */
	@Value("${limit.millis.text:一小时}")
	private String millisText;

	private static final Logger logger = LoggerFactory.getLogger(BaseUserServiceImpl.class);

	@Override
	public String selectByAccount(Map<String, Object> parameters) {
		String account=MapUtils.getString(parameters, "account");
		// 条件查询
		List<BaseUser> list = this.baseUserRepository.findByAccount(account);
		if (CollectionUtils.isEmpty(list)) {
			// 没有此用户名
			return "00";
		}
		BaseUser dataBaseUser = list.get(0);
		// 错误3次,并且时间未到
		if (dataBaseUser.getErrorCount() >= 3 && compareTo(dataBaseUser.getLastLoginTime())) {
			return "请你联系管理员，或者" + millisText + "之后再次尝试！";
		}
		// 传入的password已经md5过一次了,并且为小写
		if (!EncryptUtil.match(MapUtils.getString(parameters, "passwordIn"), dataBaseUser.getPassword())) {
			// 密码不正确
			return loginTimes(dataBaseUser, MapUtils.getString(parameters, "loginIp"));
			// return "00";
		}
		// 更新最后登录时间和登录ip
		dataBaseUser.setErrorCount( 0);
		dataBaseUser.setLastLoginTime(new Date());
		dataBaseUser.setLastLoginIp(MapUtils.getString(parameters, "loginIp"));
		this.baseUserRepository.save(dataBaseUser);
		// controller中取出放到session中
		parameters.put("baseUser", dataBaseUser);
		return "01";
	}

	@Override
	public void deleteByPrimaryKey(Long id) {
		// 删除角色中的
		this.baseUserRoleRepository.deleteByUserId(id);
		this.baseUserRepository.delete(id);
	}

	@Override
	public void updateUserPassword(Map<String, Object> parameters) {
		Long userId = MapUtils.getLong(parameters, "userId");
		String newPassword = MapUtils.getString(parameters, "newPassword");
		// 保存新密码
		BaseUser baseUsers = this.baseUserRepository.findOne(userId);
		baseUsers.setPassword(EncryptUtil.encrypt(newPassword));
		this.baseUserRepository.save(baseUsers);
	}

	@Override
	public String saveUser(BaseUser user,Collection<Long> roleIds) {
		// 判断用户名是否重复
		if (null==user.getId()) {
			//TODO: 如果是已经存在的用户，并且用户要修改用户名，则需要判断
			//criteria.put("userId", user.getId());
			List<BaseUser> list = this.baseUserRepository.findByAccount(user.getAccount());
			if (!CollectionUtils.isEmpty(list)) {
				return "帐号已经被注册！请重新填写!";
			}
			// 新建的用户,加密保存下
			user.setPassword(EncryptUtil.encrypt(user.getPassword()));
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

	@Override
	public String resetPwdByPrimaryKey(Long userId) {
		BaseUser oldUser = this.baseUserRepository.findOne(userId);
		if (oldUser == null) {
			return "没有找到该用户！";
		}
		oldUser.setErrorCount(0);// 重置下登录错误次数
		oldUser.setPassword(EncryptUtil.encrypt(resetPassword));
		return this.baseUserRepository.save(oldUser) != null ? "01" : "00";
	}

	@Override
	public void update(BaseUser user) {
		 this.baseUserRepository.save(user);
	}

	@Override
	public boolean validateAccount(String account) {
		List<BaseUser> list=this.baseUserRepository.findByAccount(account);
		if(!CollectionUtils.isEmpty(list)){
			return false;
		}
		return true;
	}

	/**
	 * 限制密码输入次数
	 * 
	 * @param baseUsers
	 * @param criteria
	 * @return
	 */
	private String loginTimes(BaseUser baseUsers, String loginIp) {
		String info = "";
		Integer errorCount = baseUsers.getErrorCount();
		Date lastLoginTime = baseUsers.getLastLoginTime();
		if (!compareTo(lastLoginTime)) {// 已经过了XX分钟，那么把errorCount设置为0，重新计数。
			errorCount =  0;
		}
		switch (errorCount) {
		case 0:
			// 第一次输入密码错误
			info = "密码错误!你还有2次机会输入密码!";
			errorCount =  errorCount + 1;
			break;
		case 1:
			// 第二次输入密码错误
			info = "密码错误!你还有1次机会输入密码!<br/>如果输入错误，帐户将被锁定不能登录！";
			errorCount =  errorCount + 1;
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
		// 60分钟1000*60*60;
		return (now - lastly) <= millis;
	}

	@Override
	public String findPassword(BaseUser user) {
		// 查询是否存在
		List<BaseUser> list = this.baseUserRepository.findByAccountAndEmail(user.getAccount(),user.getEmail());
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
	 * 
	 * 发送邮件
	 */
	private boolean execSend(String address, String title, String body) {
		Properties props = new Properties();
		// 定义邮件服务器的地址
		props.put("mail.smtp.host", emailHost);
		props.put("mail.smtp.auth", "true");
		// 取得Session
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
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
			logger.error("MessagingException:{}",e);
			throw new ServiceException(e,"邮件发送失败！","");
		}
		logger.info("向邮件地址:{}发送邮件成功！", address);
		return true;
	}

	@Override
	public Page<BaseUser> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
		PageRequest pageable = JpaTools.getPageRequest(pager, "");
		Specification<BaseUser> spec = JpaTools.getSpecification(parameters, BaseUser.class);
		return this.baseUserRepository.findAll(spec, pageable);
	}

	@Override
	public String updatePassword(Map<String, Object> parameters) {
		Long id=MapUtils.getLong(parameters, "userId");
		String password=MapUtils.getString(parameters, "password");
		BaseUser user = this.baseUserRepository.findOne(id);
		if (user == null) {
			return "00";
		}
		user.setPassword(EncryptUtil.encrypt(password));
		this.baseUserRepository.save(user);
		return "01";
	}

	@Override
	public List<BaseUserRole> selectRolesByUserId(Long userId) {
		return this.baseUserRoleRepository.findByUserId(userId);
	}

	@Override
	public BaseUser findOne(Long userId) {
		return this.baseUserRepository.findOne(userId);
	}

}