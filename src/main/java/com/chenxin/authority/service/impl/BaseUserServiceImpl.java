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
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chenxin.authority.common.utils.EncryptUtil;
import com.chenxin.authority.common.utils.JpaTools;
import com.chenxin.authority.dao.BaseUserRepository;
import com.chenxin.authority.dao.BaseUserRoleRepository;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.service.BaseUserService;
import com.google.common.collect.Maps;

//import org.apache.commons.codec.digest.DigestUtils;
@Service
public class BaseUserServiceImpl implements BaseUserService {
	@Autowired
	private BaseUserRepository baseUsersRepository;
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
	public String selectByBaseUser(Criteria criteria) {
		Map<String, Object> parameters = Maps.newHashMap();
		// 条件查询
		List<BaseUser> list = this.baseUsersRepository.selectByExample(criteria);
		if (null == list || list.size() != 1) {
			// 没有此用户名
			return "00";
		}
		BaseUser dataBaseUser = list.get(0);
		// 错误3次,并且时间未到
		if (dataBaseUser.getErrorCount() >= 3 && compareTo(dataBaseUser.getLastLoginTime())) {
			return "请你联系管理员，或者" + millisText + "之后再次尝试！";
		}
		// 传入的password已经md5过一次了,并且为小写
		if (!EncryptUtil.match(criteria.getString("passwordIn"), dataBaseUser.getPassword())) {
			// 密码不正确
			return loginTimes(dataBaseUser, criteria);
			// return "00";
		}
		// 更新最后登录时间和登录ip
		BaseUser updateUser = new BaseUser();
		updateUser.setUserId(dataBaseUser.getId());
		updateUser.setErrorCount((short) 0);
		updateUser.setLastLoginTime(new Date());
		updateUser.setLastLoginIp(criteria.getString("loginip"));
		this.baseUsersRepository.updateByPrimaryKeySelective(updateUser);
		// controller中取出放到session中
		criteria.put("baseUser", dataBaseUser);
		return "01";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String deleteByPrimaryKey(Criteria example) {
		int result = 0;
		// 删除角色中的
		this.baseUserRoleRepository.deleteByExample(example);
		String userId = example.getString("userId");
		result = this.baseUsersRepository.deleteByPrimaryKey(userId);
		return result > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String updateUserPassword(Criteria example) {
		BaseUser user = (BaseUser) example.get("user");
		String oldPassword = example.getString("oldPassword");
		String userId = example.getString("userId");
		String newPassword = example.getString("newPassword");
		// 比较原密码
		if (!userId.equals(user.getId()) || !EncryptUtil.match(oldPassword, user.getPassword())) {
			return "原密码不正确！请重新输入！";
		}
		// 保存新密码
		BaseUser baseUsers = new BaseUser();
		baseUsers.setUserId(userId);
		baseUsers.setPassword(EncryptUtil.encrypt(newPassword));
		return this.baseUsersRepository.updateByPrimaryKeySelective(baseUsers) > 0 ? "01" : "00";
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveUser(Criteria example) {
		BaseUser user = (BaseUser) example.get("user");
		Collection<String> roleIds = (Collection<String>) example.get("roleIds");
		Criteria criteria = new Criteria();
		// 判断用户名是否重复
		if (StringUtils.isNotBlank(user.getId())) {
			// 如果是已经存在的用户，并且用户要修改用户名，则需要判断
			criteria.put("userId", user.getId());
		}
		criteria.put("account", user.getAccount());
		boolean exit = this.baseUsersRepository.countByExample(criteria) > 0 ? true : false;
		if (exit) {
			return "帐号已经被注册！请重新填写!";
		}
		int result = 0;
		if (StringUtils.isBlank(user.getId())) {
			// 新建的用户
			// 加密保存下
			user.setPassword(EncryptUtil.encrypt(user.getPassword()));
			result = this.baseUsersRepository.insertSelective(user);
		} else {
			// 修改的用户
			result = this.baseUsersRepository.updateByPrimaryKeySelective(user);
		}
		if (result == 0) {
			return "00";
		}
		// 更新用户的角色信息
		criteria.clear();
		criteria.put("userId", user.getId());
		// 删除已有的用户角色信息
		this.baseUserRoleRepository.deleteByExample(criteria);
		// 保存新的角色信息
		for (String roleId : roleIds) {
			BaseUserRole role = new BaseUserRole();
			role.setRoleId(roleId);
			role.setUserId(user.getId());
			this.baseUserRoleRepository.insert(role);
		}
		return "01";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String resetPwdByPrimaryKey(Criteria example) {
		String userId = example.getString("userId");
		BaseUser oldUser = this.baseUsersRepository.selectByPrimaryKey(userId);
		if (oldUser == null) {
			return "没有找到该用户！";
		}
		BaseUser user = new BaseUser();
		user.setUserId(userId);
		user.setErrorCount((short) 0);// 重置下登录错误次数
		user.setPassword(EncryptUtil.encrypt(resetPassword));
		return this.baseUsersRepository.updateByPrimaryKeySelective(user) > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String updateByPrimaryKeySelective(BaseUser user) {
		return this.baseUsersRepository.updateByPrimaryKeySelective(user) > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String updatePassword(Criteria example) {
		BaseUser user = this.baseUsersRepository.selectByPrimaryKey(example.getString("userId"));
		if (user == null) {
			return "00";
		}
		BaseUser updateUser = new BaseUser();
		updateUser.setUserId(user.getId());
		updateUser.setPassword(EncryptUtil.encrypt(example.getString("password")));
		return this.baseUsersRepository.updateByPrimaryKeySelective(updateUser) > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String validateAccount(Criteria example) {
		return this.baseUsersRepository.countByExample(example) > 0 ? "00" : "01";
	}

	/**
	 * 限制密码输入次数
	 * 
	 * @param baseUsers
	 * @param criteria
	 * @return
	 */
	private String loginTimes(BaseUser baseUsers, Criteria criteria) {
		String info = "";
		Short errorCount = baseUsers.getErrorCount();
		Date lastLoginTime = baseUsers.getLastLoginTime();
		if (!compareTo(lastLoginTime)) {// 已经过了XX分钟，那么把errorCount设置为0，重新计数。
			errorCount = (short) 0;
		}
		switch (errorCount) {
		case 0:
			// 第一次输入密码错误
			info = "密码错误!你还有2次机会输入密码!";
			errorCount = (short) (errorCount + 1);
			break;
		case 1:
			// 第二次输入密码错误
			info = "密码错误!你还有1次机会输入密码!<br/>如果输入错误，帐户将被锁定不能登录！";
			errorCount = (short) (errorCount + 1);
			break;
		case 2:
			// 第三次输入密码错误
			info = "密码错误!你的帐户已经被锁定！<br/>请联系管理员！";
			errorCount = (short) (errorCount + 1);
			break;
		default:
			break;
		}
		// 保存新的错误次数和时间
		BaseUser updateUser = new BaseUser();
		updateUser.setUserId(baseUsers.getId());
		updateUser.setErrorCount(errorCount);
		updateUser.setLastLoginTime(new Date());
		updateUser.setLastLoginIp(criteria.getString("loginip"));
		this.baseUsersRepository.updateByPrimaryKeySelective(updateUser);
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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String findPassword(Criteria example) throws Exception {
		BaseUser user = (BaseUser) example.get("user");
		// 查询是否存在
		example.put("account", user.getAccount());
		example.put("email", user.getEmail());
		List<BaseUser> list = this.baseUsersRepository.selectByExample(example);
		if (null == list || list.size() != 1) {
			return "请输入正确的帐号和其注册邮箱！";
		}
		BaseUser dataBaseUser = list.get(0);
		// 设置token
		String token = EncryptUtil.encrypt(RandomStringUtils.randomAlphanumeric(10));

		BaseUser updateUser = new BaseUser();
		updateUser.setUserId(dataBaseUser.getId());
		updateUser.setLastLoginTime(new Date());// 记录发送连接时间
		updateUser.setPassword(token);// 设置密码
		this.baseUsersRepository.updateByPrimaryKeySelective(updateUser);

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
	private boolean execSend(String address, String title, String body) throws Exception {
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
		logger.info("向邮件地址:{}发送邮件成功！", address);
		return true;
	}

	@Override
	public Page<BaseUser> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
		PageRequest pageable = JpaTools.getPageRequest(pager, "");
		Specification<BaseUser> spec = JpaTools.getSpecification(parameters, BaseUser.class);
		return this.baseUsersRepository.findAll(spec, pageable);
	}

}