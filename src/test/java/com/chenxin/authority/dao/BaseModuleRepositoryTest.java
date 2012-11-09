package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseField;

public class BaseModuleRepositoryTest extends Dao {
	private static final Logger logger = LoggerFactory.getLogger(BaseModuleRepositoryTest.class);
	@Autowired
	private BaseModuleRepository repository;

	private BaseField baseField;

	@Before
	public void before() {
		baseField = new BaseField();
		baseField.setField("field");
		baseField.setEnabled(1);
		baseField.setFieldName("fieldName");
	}

	@Test
	public void testSelectAllModules() {
		assertNotNull(repository.selectAllModules(null));
	}
}
