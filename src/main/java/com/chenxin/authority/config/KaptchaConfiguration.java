package com.chenxin.authority.config;

import com.chenxin.authority.util.Reflections;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.ConfigException;
import com.google.code.kaptcha.util.ConfigHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by xinch on 2016/11/8.
 */
@Configuration
public class KaptchaConfiguration {


    @Value("${kaptcha.border:no}")
    private String border;

    @Value("${kaptcha.textproducer.char.space:0}")
    private String space;

    @Value("${kaptcha.textproducer.font.color:blue}")
    private String color;

    @Value("${kaptcha.noise.color:blue}")
    private String noiseColor;

    @Value("${kaptcha.textproducer.char.length:6}")
    private String length;

    @Value("${kaptcha.textproducer.font.names:Verdana,Cambria,Consolas,sans-serif,SimSun,Arial,Courier New}")
    private String fontNames;


    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.put("kaptcha.border", border);
        properties.put("kaptcha.noise.color", noiseColor);
        properties.put("kaptcha.textproducer.font.color", color);
        properties.put("kaptcha.textproducer.char.space", space);
        properties.put("kaptcha.textproducer.char.length", length);
        properties.put("kaptcha.textproducer.font.names", fontNames);
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

}
