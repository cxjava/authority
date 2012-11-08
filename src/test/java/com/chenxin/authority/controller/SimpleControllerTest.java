package com.chenxin.authority.controller;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.chenxin.authority.web.controller.UserController;

/**
 * controller基本测试类
 * 
 * @author chenxin
 * @date 2011-12-7 下午4:17:51
 */
public class SimpleControllerTest extends ControllerTest {
	@Autowired
	private UserController controller;

	@Override
	@Before
	public void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
	}

	@Test
	public void testSimple() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/user/changepwd");
		request.setParameter("oldpassword", "");
		request.setParameter("password", "");
		request.setParameter("repassword", "");
		request.setParameter("userId", "ADSF");

		handlerAdapter.handle(request, response, controller);
		assertEquals("{\"success\":false,\"msg\":\"原密码不能为空！\"}", response.getContentAsString());
	}

	@Test
	public void testAll() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/user/");
		request.setParameter("limit", "10");
		request.setParameter("start", "1");
		
		handlerAdapter.handle(request, response, controller);
		assertNotNull(response.getContentAsString());
	}
	@Test
	public void testEasyMock() throws Exception {
		request.setMethod("POST");
		request.setRequestURI("/user/");
		request.setParameter("limit", "10");
		request.setParameter("start", "1");
		controller = EasyMock.createMock(UserController.class);
		handlerAdapter.handle(request, response, controller);
		assertNotNull(response.getContentAsString());
	}
}
