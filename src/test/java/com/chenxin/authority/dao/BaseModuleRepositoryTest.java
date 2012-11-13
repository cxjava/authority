package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseField;
import com.chenxin.authority.pojo.BaseModule;

public class BaseModuleRepositoryTest extends Dao {
	private static final Logger logger = LoggerFactory.getLogger(BaseModuleRepositoryTest.class);
	@Autowired
	private BaseModuleRepository repository;

	private BaseModule module;

	@Before
	public void before() {
		module = new BaseModule();
		module.setModuleName("moduleName");
		module.setModuleUrl("moduleUrl");
	}

	@Test
	public void testSelectAllModules() {
		List<BaseModule> list=this.repository.findByUserId(1L);
		logger.info("{}",list);
		assertNotNull(list);
	}
}
