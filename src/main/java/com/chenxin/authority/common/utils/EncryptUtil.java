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
	
	private EncryptUtil(){}
	private static final PasswordEncoder encoder = new StandardPasswordEncoder("secret");

	public static String encrypt(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	public static boolean match(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}
}
