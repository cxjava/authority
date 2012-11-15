package com.chenxin.authority.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.chenxin.authority.common.utils.EncryptUtil;
import com.chenxin.authority.dao.BaseUserRepository;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.ExtPager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Maty Chen
 * 
 */
public class BaseUserServiceTest extends Services {

	// private static final Logger logger =
	// LoggerFactory.getLogger(BaseUserServiceTest.class);

	@Autowired
	private BaseUserService service;
	@Autowired
	private BaseUserRepository repository;
	private BaseUser base;

	private BaseUser getBaseUser() {
		base = new BaseUser();
		base.setSex(1);
		base.setMobile("198");
		base.setOfficePhone("22");
		base.setRemark("remark");
		base.setAccount("admins");
		base.setPassword(EncryptUtil.encrypt("desc"));
		base.setLastLoginTime(new Date());
		base.setEmail("admin@qq.com");
		base.setErrorCount(0);
		return base;
	}

	@Test
	public void testSelectByAccount() {
		Collection<Long> roleIdsCollection = Lists.newArrayList();
		base = getBaseUser();
		this.service.saveUser(base, roleIdsCollection);
		assertNotNull(base);
		// first if
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("account", "aaa");
		String result = this.service.selectByAccount(parameters);
		assertEquals(result, "00");
		// error time
		base = getBaseUser();
		base.setErrorCount(4);
		base.setAccount("dddddd");
		base.setLastLoginTime(new Date());
		base = this.repository.save(base);
		assertTrue(base.getErrorCount().intValue() >= 3);
		assertTrue(compareTo(base.getLastLoginTime()));
		assertTrue(base.getErrorCount() >= 3 && compareTo(base.getLastLoginTime()));
		parameters = Maps.newHashMap();
		parameters.put("account", "dddddd");
		result = this.service.selectByAccount(parameters);
		assertEquals(result, "请你联系管理员，或者60分钟之后再次尝试！");
		// password

		base = getBaseUser();
		base.setAccount("ffff");
		base = this.repository.save(base);
		parameters = Maps.newHashMap();
		parameters.put("account", "ffff");
		parameters.put("passwordIn", "ffff");
		result = this.service.selectByAccount(parameters);
		assertEquals(result, "密码错误!你还有2次机会输入密码!");
		result = this.service.selectByAccount(parameters);
		assertEquals(result, "密码错误!你还有1次机会输入密码!<br/>如果输入错误，帐户将被锁定不能登录！");
		result = this.service.selectByAccount(parameters);
		assertEquals(result, "密码错误!你的帐户已经被锁定！<br/>请联系管理员！");
		base = getBaseUser();
		base.setAccount("ffffee");
		base.setPassword(EncryptUtil.encrypt("c4ca4238a0b923820dcc509a6f75849b"));
		base = this.repository.save(base);
		parameters = Maps.newHashMap();
		parameters.put("account", "ffffee");
		parameters.put("passwordIn", "c4ca4238a0b923820dcc509a6f75849b");
		result = this.service.selectByAccount(parameters);
		assertEquals(result, "01");
	}

	@Test
	public void testDeleteByPrimaryKey() {
		try {
			this.service.deleteByPrimaryKey(1L);
		} catch (ServiceException e) {
			assertEquals("不能删除超级管理员！", e.getMessage());
		}
		Collection<Long> roleIdsCollection = Lists.newArrayList();
		BaseUser user = this.getBaseUser();
		this.service.saveUser(user, roleIdsCollection);
		this.service.deleteByPrimaryKey(user.getId());
	}

	@Test
	public void testUpdateUserPassword() {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("userId", "1");
		parameters.put("newPassword", "test");
		this.service.updateUserPassword(parameters);
	}

	@Test
	public void testSaveUser() {
		Collection<Long> roleIdsCollection = Lists.newArrayList();
		BaseUser baseUser = this.getBaseUser();
		baseUser.setAccount("admin");
		String result = this.service.saveUser(baseUser, roleIdsCollection);
		assertEquals(result, "帐号已经被注册！请重新填写!");

		baseUser = this.getBaseUser();
		baseUser.setAccount("ddd");
		roleIdsCollection.add(1L);
		roleIdsCollection.add(2L);
		result = this.service.saveUser(baseUser, roleIdsCollection);

		assertEquals(result, "01");

		baseUser = this.getBaseUser();
		baseUser.setAccount("ddd");
		baseUser.setId(1L);
		roleIdsCollection.add(1L);
		roleIdsCollection.add(2L);
		result = this.service.saveUser(baseUser, roleIdsCollection);

		assertEquals(result, "01");
	}

	@Test
	public void testResetPwdByPrimaryKey() {
		String result = this.service.resetPwdByPrimaryKey(0L);
		assertEquals(result, "没有找到该用户！");
		result = this.service.resetPwdByPrimaryKey(1L);
		assertEquals(result, "01");

	}

	@Test
	public void testUpdate() {
		BaseUser user = this.getBaseUser();
		user.setId(1L);
		this.service.update(user);
	}

	@Test
	public void testValidateAccount() {
		assertTrue(!this.service.validateAccount("admin"));
		assertTrue(this.service.validateAccount("test1"));
	}



	private boolean compareTo(Date date) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.setTime(date);
		long lastly = c.getTimeInMillis();
		return (now - lastly) <= 3600000;
	}

	@Test
	public void testSelectByParameters() {
		Collection<Long> roleIdsCollection = Lists.newArrayList();

		this.service.saveUser(getBaseUser(), roleIdsCollection);
		this.service.saveUser(getBaseUser(), roleIdsCollection);
		this.service.saveUser(getBaseUser(), roleIdsCollection);
		ExtPager pager = new ExtPager();

		pager.setLimit(10);
		pager.setStart(0);
		pager.setDir("desc");
		pager.setSort("realName");
		Map<String, Object> parameters = Maps.newHashMap();

		Page<BaseUser> pages = this.service.selectByParameters(pager, parameters);
		assertNotNull(pages);
		assertTrue(pages.getTotalElements() >= 3);
		assertNotNull(pages.getContent());
		assertTrue(pages.getContent().size() >= 3);
	}

	@Test
	public void testUpdatePassword() {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("userId", "0");
		parameters.put("password", "password");
		String str = this.service.updatePassword(parameters);
		assertEquals(str, "00");
		parameters = Maps.newHashMap();
		parameters.put("userId", "1");
		parameters.put("password", "password");
		str = this.service.updatePassword(parameters);
		assertEquals(str, "01");

	}

	@Test
	public void testSelectRolesByUserId() {
		List<BaseUserRole> list = this.service.selectRolesByUserId(1L);
		assertNotNull(list);
		assertNotNull(list.size());
		assertTrue(list.size() >= 1);
	}

	@Test
	public void testFindOne() {
		base = this.service.findOne(1L);
		assertNotNull(base);
		assertNotNull(base.getId());
		assertEquals(base.getAccount(), "admin");
	}
	@Test
	public void testFindPassword() {
		base = this.getBaseUser();
		String str = this.service.findPassword(base);
		assertEquals(str, "请输入正确的帐号和其注册邮箱！");
		base = this.getBaseUser();
		base.setAccount("admin");
		base.setEmail("admin@qq.com.cn");
		try {
			str = this.service.findPassword(base);
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "邮件发送失败！");
		}
	}
}
