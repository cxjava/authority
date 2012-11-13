package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseUser;

public class BaseUserRepositoryTest extends Dao {
	private static final Logger logger = LoggerFactory.getLogger(BaseUserRepositoryTest.class);
	@Autowired
	private BaseUserRepository repository;

	private BaseUser user;

	@Before
	public void before() {
		user = new BaseUser();
		user.setAccount("admin");
		user.setPassword("1");
		user.setLastLoginTime(new Date());
	}

	@Test
	public void testFindOne() {
		user = repository.findOne(1L);
		// assertNotNull(list);
		// assertEquals(list.size(), 1);

	}
}
