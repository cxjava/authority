package com.chenxin.authority.web.interseptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 验证用户登陆拦截器
 * 
 * @author Maty Chen
 * @date 2011-3-13 下午09:02:00
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Object user = request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// 如果session中没有user对象
		if (null == user) {
			String requestedWith = request.getHeader("x-requested-with");
			// ajax请求
			if (requestedWith != null && "XMLHttpRequest".equals(requestedWith)) {
				response.setHeader("session-status", "timeout");
				response.getWriter().print(WebConstants.TIME_OUT);
			} else {
				// 普通页面请求
				response.sendRedirect(request.getContextPath() + "/");
			}
			return false;
		} else {
			// BaseUser users=(BaseUser)user;
			// Object[] args=new
			// Object[]{users.getAccount(),users.getRealName(),request.getRequestURI(),request.getRequestURL()};
			// logger.debug("start");
			// logger.debug("getAccount:{},getRealName:{},getRequestURI:{},getRequestURL:{},",args);
			// logger.debug("end");
		}
		return true;

	}

}
