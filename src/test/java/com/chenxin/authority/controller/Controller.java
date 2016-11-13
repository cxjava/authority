package com.chenxin.authority.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * controller基本测试类
 * 
 * @author Maty Chen
 * @date 2011-12-7 下午4:17:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("H2")
@WebAppConfiguration
@SpringBootTest
public abstract class Controller {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private static final ObjectMapper OM = new ObjectMapper();

	@Autowired
	protected WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	protected String Obj2JSON(Object o) {
		try {
			return OM.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException:{}", e);
		}
		return null;
	}

}
