package com.chenxin.authority.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chenxin.authority.common.utils.EncryptUtil;
import com.chenxin.authority.dao.BaseUserRoleMapper;
import com.chenxin.authority.dao.BaseUsersMapper;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.BaseUsers;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.service.BaseUsersService;

//import org.apache.commons.codec.digest.DigestUtils;
@Service
public class BaseUsersServiceImpl implements BaseUsersService {
	@Autowired
	private BaseUsersMapper baseUsersMapper;
	@Autowired
	private BaseUserRoleMapper baseUserRoleMapper;
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

	private static final Logger logger = LoggerFactory.getLogger(BaseUsersServiceImpl.class);

	@Override
	public String selectByBaseUser(Criteria criteria) {
		// 条件查询
		List<BaseUsers> list = this.baseUsersMapper.selectByExample(criteria);
		if (null == list || list.size() != 1) {
			// 没有此用户名
			return "00";
		}
		BaseUsers dataBaseUser = list.get(0);
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
		BaseUsers updateUser = new BaseUsers();
		updateUser.setUserId(dataBaseUser.getUserId());
		updateUser.setErrorCount((short) 0);
		updateUser.setLastLoginTime(new Date());
		updateUser.setLastLoginIp(criteria.getString("loginip"));
		this.baseUsersMapper.updateByPrimaryKeySelective(updateUser);
		// controller中取出放到session中
		criteria.put("baseUser", dataBaseUser);
		return "01";
	}

	@Override
	public int countByExample(Criteria example) {
		int count = this.baseUsersMapper.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseUsers selectByPrimaryKey(String userId) {
		return this.baseUsersMapper.selectByPrimaryKey(userId);
	}

	@Override
	public List<BaseUsers> selectByExample(Criteria example) {
		return this.baseUsersMapper.selectByExample(example);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String deleteByPrimaryKey(Criteria example) {
		int result = 0;
		// 删除角色中的
		this.baseUserRoleMapper.deleteByExample(example);
		String userId = example.getString("userId");
		result = this.baseUsersMapper.deleteByPrimaryKey(userId);
		return result > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String updateUserPassword(Criteria example) {
		BaseUsers user = (BaseUsers) example.get("user");
		String oldPassword = example.getString("oldPassword");
		String userId = example.getString("userId");
		String newPassword = example.getString("newPassword");
		// 比较原密码
		if (!userId.equals(user.getUserId()) || !EncryptUtil.match(oldPassword, user.getPassword())) {
			return "原密码不正确！请重新输入！";
		}
		// 保存新密码
		BaseUsers baseUsers = new BaseUsers();
		baseUsers.setUserId(userId);
		baseUsers.setPassword(EncryptUtil.encrypt(newPassword));
		return this.baseUsersMapper.updateByPrimaryKeySelective(baseUsers) > 0 ? "01" : "00";
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveUser(Criteria example) {
		BaseUsers user = (BaseUsers) example.get("user");
		Collection<String> roleIds = (Collection<String>) example.get("roleIds");
		Criteria criteria = new Criteria();
		// 判断用户名是否重复
		if (StringUtils.isNotBlank(user.getUserId())) {
			// 如果是已经存在的用户，并且用户要修改用户名，则需要判断
			criteria.put("userId", user.getUserId());
		}
		criteria.put("account", user.getAccount());
		boolean exit = this.baseUsersMapper.countByExample(criteria) > 0 ? true : false;
		if (exit) {
			return "帐号已经被注册！请重新填写!";
		}
		int result = 0;
		if (StringUtils.isBlank(user.getUserId())) {
			// 新建的用户
			// 加密保存下
			user.setPassword(EncryptUtil.encrypt(user.getPassword()));
			result = this.baseUsersMapper.insertSelective(user);
		} else {
			// 修改的用户
			result = this.baseUsersMapper.updateByPrimaryKeySelective(user);
		}
		if (result == 0) {
			return "00";
		}
		// 更新用户的角色信息
		criteria.clear();
		criteria.put("userId", user.getUserId());
		// 删除已有的用户角色信息
		this.baseUserRoleMapper.deleteByExample(criteria);
		// 保存新的角色信息
		for (String roleId : roleIds) {
			BaseUserRole role = new BaseUserRole();
			role.setRoleId(roleId);
			role.setUserId(user.getUserId());
			this.baseUserRoleMapper.insert(role);
		}
		return "01";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String resetPwdByPrimaryKey(Criteria example) {
		String userId = example.getString("userId");
		BaseUsers oldUser = this.baseUsersMapper.selectByPrimaryKey(userId);
		if (oldUser == null) {
			return "没有找到该用户！";
		}
		BaseUsers user = new BaseUsers();
		user.setUserId(userId);
		user.setErrorCount((short) 0);// 重置下登录错误次数
		user.setPassword(EncryptUtil.encrypt(resetPassword));
		return this.baseUsersMapper.updateByPrimaryKeySelective(user) > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String updateByPrimaryKeySelective(BaseUsers user) {
		return this.baseUsersMapper.updateByPrimaryKeySelective(user) > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String updatePassword(Criteria example) {
		BaseUsers user = this.baseUsersMapper.selectByPrimaryKey(example.getString("userId"));
		if (user == null) {
			return "00";
		}
		BaseUsers updateUser = new BaseUsers();
		updateUser.setUserId(user.getUserId());
		updateUser.setPassword(EncryptUtil.encrypt(example.getString("password")));
		return this.baseUsersMapper.updateByPrimaryKeySelective(updateUser) > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String validateAccount(Criteria example) {
		return this.baseUsersMapper.countByExample(example) > 0 ? "00" : "01";
	}

	/**
	 * 限制密码输入次数
	 * 
	 * @param baseUsers
	 * @param criteria
	 * @return
	 */
	private String loginTimes(BaseUsers baseUsers, Criteria criteria) {
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
		BaseUsers updateUser = new BaseUsers();
		updateUser.setUserId(baseUsers.getUserId());
		updateUser.setErrorCount(errorCount);
		updateUser.setLastLoginTime(new Date());
		updateUser.setLastLoginIp(criteria.getString("loginip"));
		this.baseUsersMapper.updateByPrimaryKeySelective(updateUser);
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
		BaseUsers user = (BaseUsers) example.get("user");
		// 查询是否存在
		example.put("account", user.getAccount());
		example.put("email", user.getEmail());
		List<BaseUsers> list = this.baseUsersMapper.selectByExample(example);
		if (null == list || list.size() != 1) {
			return "请输入正确的帐号和其注册邮箱！";
		}
		BaseUsers dataBaseUser = list.get(0);
		// 设置token
		String token = EncryptUtil.encrypt(RandomStringUtils.randomAlphanumeric(10));

		BaseUsers updateUser = new BaseUsers();
		updateUser.setUserId(dataBaseUser.getUserId());
		updateUser.setLastLoginTime(new Date());// 记录发送连接时间
		updateUser.setPassword(token);// 设置密码
		this.baseUsersMapper.updateByPrimaryKeySelective(updateUser);

		String title = "亲爱的 " + dataBaseUser.getAccount() + "，请重新设置你的帐户密码！";
		String url = systemUrl + token.toUpperCase() + "/" + dataBaseUser.getUserId();
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

}