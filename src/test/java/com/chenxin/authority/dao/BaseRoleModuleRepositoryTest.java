package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseRoleModule;

public class BaseRoleModuleRepositoryTest extends Dao {
	@Autowired
	private BaseRoleModuleRepository repository;

	private BaseRoleModule object;

	private List<BaseRoleModule> list;

	@Before
	public void before() {
		object = new BaseRoleModule();
		object.setRoleId(1L);
		object.setModuleId(2L);
	}

	@Test
	public void testFindByEnabled() {
		object = repository.save(object);
		assertNotNull(object.getId());
		int result = repository.deleteByRoleId(1L);
		assertNotNull(result >= 1);
	}

	@Test
	public void testFindByRoleId() {
		object = repository.save(object);
		assertNotNull(object.getId());
		list = repository.findByRoleId(1L);
		assertNotNull(list);
		assertNotNull(list.size() >= 1);
	}
}
