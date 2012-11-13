package com.chenxin.authority.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 * controller基本测试类
 * 
 * @author Maty Chen
 * @date 2011-12-7 下午4:17:51
 */
@ContextConfiguration(locations = { "classpath:/config/spring/spring*.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("H2")
public class Controller {
	@Autowired
	protected AnnotationMethodHandlerAdapter handlerAdapter;
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;

	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
	}

}
