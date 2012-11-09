package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseField;

public class BaseFieldRepositoryTest extends Dao {
	private static final Logger logger = LoggerFactory.getLogger(BaseFieldRepositoryTest.class);
	@Autowired
	private BaseFieldRepository repository;

	private BaseField baseField;

	@Before
	public void before() {
		baseField = new BaseField();
		baseField.setField("field");
		baseField.setEnabled(1);
		baseField.setFieldName("fieldName");
	}

	@Test
	public void testSave() {
		repository.deleteAll();
		baseField = repository.save(baseField);
		assertNotNull(baseField);
		logger.info(baseField.toString());
		List<BaseField> list = repository.findByEnabled(1);
		assertNotNull(list);
		assertEquals(list.size(), 1);
	}
}
