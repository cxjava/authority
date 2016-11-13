package com.chenxin.authority.util;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * spring的加密
 *
 * @author cx
 * @date 2012-2-29 上午10:52:20
 */
public class EncryptUtil {

    protected EncryptUtil() {
    }

    private static final HashFunction hashing = Hashing.sha512();

    public static String encrypt(String rawPassword) {
       return  hashing.hashString(rawPassword, Charsets.UTF_8).toString();
    }

    public static boolean match(String rawPassword, String encodedPassword) {
        return hashing.hashString(rawPassword, Charsets.UTF_8).toString().equals(encodedPassword);
    }
}
