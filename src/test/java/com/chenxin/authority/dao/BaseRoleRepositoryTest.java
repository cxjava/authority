package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.entity.BaseRole;

public class BaseRoleRepositoryTest extends Dao {
	@Autowired
	private BaseRoleRepository repository;

	private BaseRole object;

	@Before
	public void before() {
		object = new BaseRole();
		object.setRoleDesc("desc");
		object.setRoleName("roleName");
	}

	@Test
	public void testSave() {
		object = repository.save(object);
		assertNotNull(object.getId());
	}
}
