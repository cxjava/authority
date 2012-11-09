package com.chenxin.authority.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.chenxin.authority.common.springmvc.SpringContextHolder;
import com.chenxin.authority.service.BaseFieldService;

/**
 * 系统初始化监听器
 * 
 * @author Maty Chen
 * @date 2011-12-16 下午11:26:14
 */
public class SystemInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		BaseFieldService baseFieldsService = SpringContextHolder.getBean("baseFieldServiceImpl");
		servletContext.setAttribute("fields", baseFieldsService.selectAll());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
