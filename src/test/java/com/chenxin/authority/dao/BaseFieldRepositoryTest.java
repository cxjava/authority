package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseField;

public class BaseFieldRepositoryTest extends Dao {
	@Autowired
	private BaseFieldRepository repository;

	private BaseField object;

	private List<BaseField> list;

	@Before
	public void before() {
		object = new BaseField();
		object.setField("field");
		object.setEnabled(1);
		object.setFieldName("fieldName");
	}

	@Test
	public void testFindByEnabled() {
		object = repository.save(object);
		assertNotNull(object.getId());
		list = repository.findByEnabled(1);
		assertNotNull(list);
		assertNotNull(list.size() >= 1);
	}
}
