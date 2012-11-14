package com.chenxin.authority.service;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.chenxin.authority.pojo.BaseRole;
import com.chenxin.authority.pojo.ExtPager;
import com.google.common.collect.Maps;

/**
 * @author Maty Chen
 * 
 */
public class BaseRoleServiceTest extends Services {

	// private static final Logger logger =
	// LoggerFactory.getLogger(BaseRoleServiceTest.class);

	@Autowired
	private BaseRoleService service;
	private BaseRole base;

	@Before
	public void before() {
		base = new BaseRole();
		base.setRoleName("admins");
		base.setRoleDesc("desc");
	}

	private BaseRole getBaseRole() {
		base = new BaseRole();
		base.setRoleName("admins");
		base.setRoleDesc("desc");
		return base;
	}

	@Test
	public void testSaveRole() {
		this.service.saveRole(base);
		assertNotNull(base);
		assertNotNull(base.getId());
	}

	@Test
	public void testSelectByParameters() {
		this.service.saveRole(getBaseRole());
		this.service.saveRole(getBaseRole());
		this.service.saveRole(getBaseRole());
		ExtPager pager = new ExtPager();
		Map<String, Object> parameters = Maps.newHashMap();

		pager.setLimit(10);
		pager.setStart(0);
		pager.setDir("desc");
		pager.setSort("roleName");

		Page<BaseRole> pages = this.service.selectByParameters(pager, parameters);
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
		this.service.saveRole(base);
		this.service.deleteByPrimaryKey(base.getId());
	}
}
