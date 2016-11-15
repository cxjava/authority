package com.chenxin.authority.config;

import com.google.common.base.Charsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Created by xinch on 2016/11/8.
 */
@Configuration
public class FileUploadConfiguration {
    @Bean
    CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20 * 1024 * 1024);// 20MB
        multipartResolver.setMaxInMemorySize(2 * 1024 * 1024);// 2MB
        multipartResolver.setDefaultEncoding(Charsets.UTF_8.toString());
        return multipartResolver;
    }
}
