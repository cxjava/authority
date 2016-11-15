package com.chenxin.authority.config;

import com.chenxin.authority.service.BaseFieldService;
import com.chenxin.authority.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 系统初始化监听器
 *
 * @author Maty Chen
 * @date 2011-12-16 下午11:26:14
 */
@Configuration
public class SystemInitListener implements ServletContextListener {

    //只是为了初始化
    @Autowired
    private SpringContextHolder springContextHolder;
    
    @Autowired
    private BaseFieldService baseFieldService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
//        BaseFieldService baseFieldsService = SpringContextHolder.getBean("baseFieldService");
        servletContext.setAttribute("fields", baseFieldService.selectAll());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext销毁");
    }

}