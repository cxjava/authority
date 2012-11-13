package com.chenxin.authority.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseRole;

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
	}

	@Test
	public void testSave() {
		this.service.saveRole(base);
		assertNotNull(base);
		assertNotNull(base.getId());
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
