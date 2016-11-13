package com.chenxin.authority.runner;

import com.chenxin.authority.service.BaseFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by xinch on 2016/11/8.
 */
@Component
@Order(value = 1)
public class SystemInit implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SystemInit.class);

    @Autowired
    BaseFieldService baseFieldService;

    @Override
    public void run(String... args) throws Exception {
        logger.debug(">>>>>>>>>>>>>>>runner服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
        logger.debug("{}", baseFieldService.selectAll());
    }

}
