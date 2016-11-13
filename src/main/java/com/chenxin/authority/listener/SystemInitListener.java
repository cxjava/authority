package com.chenxin.authority.listener;

import com.chenxin.authority.service.BaseFieldService;
import com.chenxin.authority.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 系统初始化监听器
 *
 * @author Maty Chen
 * @date 2011-12-16 下午11:26:14
 */
@WebListener
public class SystemInitListener implements ServletContextListener {

    //只是为了初始化
    @Autowired
    private SpringContextHolder springContextHolder;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        BaseFieldService baseFieldsService = SpringContextHolder.getBean("baseFieldService");
        servletContext.setAttribute("fields", baseFieldsService.selectAll());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext销毁");
    }

}