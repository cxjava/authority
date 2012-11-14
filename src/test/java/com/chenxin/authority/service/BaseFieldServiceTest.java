package com.chenxin.authority.service;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.chenxin.authority.pojo.BaseField;
import com.chenxin.authority.pojo.ExtPager;
import com.google.common.collect.Maps;

/**
 * @author Maty Chen
 * 
 */
public class BaseFieldServiceTest extends Services {

	private static final Logger logger = LoggerFactory.getLogger(BaseFieldServiceTest.class);

	@Autowired
	private BaseFieldService service;
	private BaseField base;

	@Before
	public void before() {
		base = new BaseField();
		base.setField("111");
		base.setFieldName("333");
	}

	private BaseField getBaseField() {
		base = new BaseField();
		base.setField("111");
		base.setFieldName("333");
		return base;
	}

	@Test
	public void testSelectAll() {
		Map<String, String> values = service.selectAll();
		logger.info(values.toString());
		assertNotNull(values);
	}

	@Test
	public void testSaveField() {
		base = this.service.saveField(base);
		assertNotNull(base.getId());
	}

	@Test
	public void testDelete() {
		base = this.service.saveField(base);
		this.service.delete(base.getId());
	}

	@Test
	public void testSelectByParameters() {
		this.service.saveField(this.getBaseField());
		this.service.saveField(this.getBaseField());
		this.service.saveField(this.getBaseField());

		ExtPager pager = new ExtPager();
		Map<String, Object> parameters = Maps.newHashMap();

		pager.setLimit(10);
		pager.setStart(0);
		pager.setDir("desc");
		pager.setSort("fieldName");
		parameters.put("", "");
		Page<BaseField> pages = this.service.selectByParameters(pager, parameters);
		assertNotNull(pages);
		assertTrue(pages.getTotalElements() >= 3);
		assertNotNull(pages.getContent());
		assertTrue(pages.getContent().size() >= 3);
	}
}
