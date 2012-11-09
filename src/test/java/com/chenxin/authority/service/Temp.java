package com.chenxin.authority.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseField;

/**
 * @author Maty Chen
 * 
 */
public class Temp extends Services {

	private static final Logger logger = LoggerFactory.getLogger(Temp.class);

	@Autowired
	private BaseFieldService service;
	private BaseField base;

	@Before
	public void before() {
		base = new BaseField();
		base.setField("");
	}

	@Test
	public void test() {
		// this.service.save()
		assertNotNull("");
	}
}
