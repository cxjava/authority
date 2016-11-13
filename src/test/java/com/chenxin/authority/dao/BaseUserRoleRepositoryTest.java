package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.entity.BaseUserRole;

public class BaseUserRoleRepositoryTest extends Dao {
	@Autowired
	private BaseUserRoleRepository repository;

	private BaseUserRole object;

	private List<BaseUserRole> list;

	@Before
	public void before() {
		object = new BaseUserRole();
		object.setRoleId(1L);
		object.setUserId(1L);
	}

	@Test
	public void testFindByRoleId() {
		object = repository.save(object);
		assertNotNull(object.getId());
		long result = repository.findByRoleId(1L);
		assertNotNull(result >= 1);
	}

	@Test
	public void testDeleteByUserId() {
		object = repository.save(object);
		assertNotNull(object.getId());
		int result = repository.deleteByUserId(1L);
		assertNotNull(result >= 1);
	}

	@Test
	public void testFindByUserId() {
		object = repository.save(object);
		assertNotNull(object.getId());
		list = repository.findByUserId(1L);
		assertNotNull(list);
		assertNotNull(list.size() >= 1);
	}
}
