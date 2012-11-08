package com.chenxin.authority.common.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * spring的加密
 * 
 * @author cx
 * @date 2012-2-29 上午10:52:20
 */
public class EncryptUtil {
	/** 从配置文件中获得密钥（SITE_WIDE_SECRET） */
	private static final String SITE_WIDE_SECRET = PropertiesHolder.getProperty("secret");
	private static final PasswordEncoder encoder = new StandardPasswordEncoder(SITE_WIDE_SECRET);

	public static String encrypt(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	public static boolean match(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}
}
