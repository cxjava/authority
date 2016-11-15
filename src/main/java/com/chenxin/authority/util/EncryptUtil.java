package com.chenxin.authority.util;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * spring的加密
 *
 * @author cx
 * @date 2012-2-29 上午10:52:20
 */
@Component
public class EncryptUtil {

    @Value("${encrypt.key:925e93b87e23e34a7e0782dd9035e787faff924760ca98fa07787e0d8315}")
    private static String key;

    protected EncryptUtil() {
    }

    private static final HashFunction hashing = Hashing.sha512();

    public static String encrypt(String rawPassword) {
        return hashing.hashString(key + rawPassword, Charsets.UTF_8).toString();
    }

    public static boolean match(String rawPassword, String encodedPassword) {
        return hashing.hashString(key + rawPassword, Charsets.UTF_8).toString().equals(encodedPassword);
    }
}
