package com.chenxin.authority.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.chenxin.authority.interseptor.LoginInterceptor;
import com.chenxin.authority.util.Reflections;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.ConfigException;
import com.google.code.kaptcha.util.ConfigHelper;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xinch on 2016/11/8.
 */
@EnableWebMvc
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
    private static final String DATA_FORMATE = "yyyy-MM-dd HH:mm:ss";


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
                        "/findpwd*", "/error*", "/", "/druid**", "/resetpwd**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/", "classpath:/static/favicon.ico");
        super.addResourceHandlers(registry);
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
        properties.put("kaptcha.textproducer.char.space", "-2");
        properties.put("kaptcha.textproducer.char.length", "6");
        properties.put("kaptcha.textproducer.font.names", "华文彩云,华文行楷,方正舒体,华文隶书,幼圆,华文琥珀");
        Config config = new Config(properties);
        ConfigHelper configHelper = new ConfigHelper() {
            @Override
            public int getPositiveInt(String paramName, String paramValue,
                                      int defaultInt) {
                int intValue;
                if ("".equals(paramValue) || paramValue == null) {
                    intValue = defaultInt;
                } else {
                    try {
                        intValue = Integer.parseInt(paramValue);
//                        if (intValue < 1)
//                        {
//                            throw new ConfigException(paramName, paramValue,
//                                    "Value must be greater than or equals to 1.");
//                        }
                    } catch (NumberFormatException nfe) {
                        throw new ConfigException(paramName, paramValue, nfe);
                    }
                }
                return intValue;
            }
        };

        Reflections.setFieldValue(config, "helper", configHelper);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

    /**
     * fastJson相关设置
     */
    private FastJsonConfig getFastJsonConfig() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 在serializerFeatureList中添加转换规则
        List<SerializerFeature> serializerFeatureList = new ArrayList<>();
        serializerFeatureList.add(SerializerFeature.WriteMapNullValue);
        serializerFeatureList.add(SerializerFeature.WriteNullStringAsEmpty);
        serializerFeatureList.add(SerializerFeature.PrettyFormat);

        fastJsonConfig.setDateFormat(DATA_FORMATE);

        SerializerFeature[] serializerFeatures = serializerFeatureList.toArray(new SerializerFeature[serializerFeatureList.size()]);
        fastJsonConfig.setSerializerFeatures(serializerFeatures);

        return fastJsonConfig;
    }

    /**
     * fastJson相关设置
     */
    private FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter4();

        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
        supportedMediaTypes.add(MediaType.parseMediaType("application/json"));

        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(getFastJsonConfig());

        return fastJsonHttpMessageConverter;
    }
    /**
     * 添加fastJsonHttpMessageConverter到converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.debug("Add fastJsonHttpMessageConverter into converters.");
        converters.add(fastJsonHttpMessageConverter());
        super.configureMessageConverters(converters);
    }
}