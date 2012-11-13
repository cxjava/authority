package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseUser;

public class BaseUserRepositoryTest extends Dao {
	@Autowired
	private BaseUserRepository repository;

	private BaseUser object;

	private List<BaseUser> list;

	@Before
	public void before() {
		object = new BaseUser();
		object.setAccount("admins");
		object.setPassword("1");
		object.setEmail("admin@qq.com");
		object.setLastLoginTime(new Date());
	}

	@Test
	public void testFindByAccount() {
		object = repository.save(object);
		assertNotNull(object.getId());
		list = repository.findByAccount("admins");
		assertNotNull(list);
		assertNotNull(list.size() >= 1);
		assertEquals(list.get(0).getAccount(), "admins");
	}
	@Test
	public void testFindByAccountAndEmail() {
		object = repository.save(object);
		assertNotNull(object.getId());
		list = repository.findByAccountAndEmail("admins","admin@qq.com");
		assertNotNull(list);
		assertNotNull(list.size() >= 1);
		assertEquals(list.get(0).getAccount(), "admins");
		assertEquals(list.get(0).getEmail(), "admin@qq.com");
	}
}
