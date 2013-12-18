package com.chenxin.authority.web.interseptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 验证用户登陆拦截器
 *
 * @author Maty Chen
 * @date 2011-3-13 下午09:02:00
 */
public class PjaxInterceptor extends HandlerInterceptorAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PjaxInterceptor.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
		logger.info(modelAndView.getViewName());
		logger.info(request.getHeader("X-PJAX"));
		if(!"true".equals(request.getHeader("X-PJAX"))){
			modelAndView.addObject("path","/WEB-INF/views/"+ modelAndView.getViewName()+".jsp");
			modelAndView.setViewName("temp");
		}
	}



}
