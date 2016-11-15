package com.chenxin.authority.config;

import com.chenxin.authority.util.Reflections;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.ConfigException;
import com.google.code.kaptcha.util.ConfigHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by xinch on 2016/11/8.
 */
@Configuration
public class KaptchaConfiguration {
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
}
