package com.chenxin.authority.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.chenxin.authority.common.utils.EncryptUtil;
import com.chenxin.authority.dao.BaseUserRepository;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.ExtPager;
import com.google.common.collect.Collections2;
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
		Collection<Long> roleIdsCollection=Lists.newArrayList();
		base=getBaseUser();
		this.service.saveUser(base,roleIdsCollection);
		assertNotNull(base);
		//first if
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("account", "aaa");
		String result=this.service.selectByAccount(parameters);
		assertEquals(result, "00");
		//error time
		base=getBaseUser();
		base.setErrorCount(4);
		base.setAccount("dddddd");
		long aa=new Date().getTime();
		base.setLastLoginTime(new Date());
		base=this.repository.save(base);
		assertTrue(base.getErrorCount().intValue()>=3);
		assertTrue(compareTo(base.getLastLoginTime()));
		assertTrue(base.getErrorCount() >= 3 && compareTo(base.getLastLoginTime()));
		parameters = Maps.newHashMap();
		parameters.put("account", "dddddd");
		 result=this.service.selectByAccount(parameters);
		assertEquals(result, "请你联系管理员，或者60分钟之后再次尝试！");
		//password
		
		base=getBaseUser();
		base.setAccount("ffff");
		base=this.repository.save(base);
		parameters = Maps.newHashMap();
		parameters.put("account", "ffff");
		parameters.put("passwordIn", "ffff");
		result=this.service.selectByAccount(parameters);
		assertEquals(result, "密码错误!你还有2次机会输入密码!");
		result=this.service.selectByAccount(parameters);
		assertEquals(result, "密码错误!你还有1次机会输入密码!<br/>如果输入错误，帐户将被锁定不能登录！");
		result=this.service.selectByAccount(parameters);
		assertEquals(result, "密码错误!你的帐户已经被锁定！<br/>请联系管理员！");
		base=getBaseUser();
		base.setAccount("ffffee");
		base=this.repository.save(base);
		parameters = Maps.newHashMap();
		parameters.put("account", "ffffee");
		parameters.put("passwordIn", "");
		result=this.service.selectByAccount(parameters);
		assertEquals(result, "01");
		
	}

	@Test
	public void testSelectByParameters() {
		this.service.update(getBaseUser());
		this.service.update(getBaseUser());
		this.service.update(getBaseUser());
		ExtPager pager = new ExtPager();

		pager.setLimit(10);
		pager.setStart(0);
		pager.setDir("desc");
		pager.setSort("roleName");
		Map<String, Object> parameters = Maps.newHashMap();

		Page<BaseUser> pages = this.service.selectByParameters(pager, parameters);
		assertNotNull(pages);
		assertTrue(pages.getTotalElements() >= 3);
		assertNotNull(pages.getContent());
		assertTrue(pages.getContent().size() >= 3);
	}

	@Test
	public void testDeleteByPrimaryKey() {
		try {
			this.service.deleteByPrimaryKey(1L);
		} catch (ServiceException e) {
			assertEquals("其他用户拥有该角色，还不能删除!", e.getMessage());
		}
		this.service.update(base);
		this.service.deleteByPrimaryKey(base.getId());
	}
	
	
	private boolean compareTo(Date date) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.setTime(date);
		long lastly = c.getTimeInMillis();
		return (now - lastly) <= 3600000;
	}

}
