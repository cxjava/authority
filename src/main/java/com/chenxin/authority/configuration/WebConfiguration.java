package com.chenxin.authority.configuration;

import com.chenxin.authority.interseptor.LoginInterceptor;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.common.base.Charsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Properties;

/**
 * Created by xinch on 2016/11/8.
 */
@EnableWebMvc
@ComponentScan
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/500").setViewName("commons/timeout");
        registry.addViewController("/error").setViewName("commons/timeout");
        registry.addViewController("/404").setViewName("commons/404");
        registry.addViewController("/").setViewName("redirect:/login");
        super.addViewControllers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/**/favicon.ico*", "/login*", "/logout*", "/checkimage.jpg*",
                        "/findpwd*", "/error*", "/","/druid**","/resetpwd**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/", "classpath:/static/favicon.ico");
        super.addResourceHandlers(registry);
    }

    @Bean
    public ViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20971520); // 20MB
        multipartResolver.setMaxInMemorySize(1048576); // 1MB
        multipartResolver.setDefaultEncoding(Charsets.UTF_8.toString());
        return multipartResolver;
    }

    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.put("kaptcha.textproducer.font.color", "blue");
        properties.put("kaptcha.noise.color", "blue");
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.char.space", "1");
        properties.put("kaptcha.textproducer.char.length", "6");
        properties.put("kaptcha.textproducer.font.names", "华文彩云,华文行楷,方正舒体,华文隶书,幼圆,华文琥珀");
        Config config = new Config(properties);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

}