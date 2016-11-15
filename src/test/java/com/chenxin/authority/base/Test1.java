package com.chenxin.authority.base;

import com.chenxin.authority.entity.Table;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @author Maty Chen
 * @date 2012-5-16 下午10:14:21
 */
public class Test1 {

    static String test = "abcdefghijklmnopqrstuvwxyz";
    static char[] source = test.toCharArray();
    static Random random = new Random();

    public static void main(String[] args) {
        //test1();
        test2();
    }

    public static void test2() {
        String aa = "realName";
        System.out.println(Table.toClumn(aa));
    }

    public static String toCamelCase(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        StringBuffer hs = new StringBuffer();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '.' || c == '_') {
                if (++i < chars.length) {
                    c = chars[i];
                    if (c >= 97 && c <= 122)
                        hs.append((char) (c - 32));
                    else
                        hs.append(c);
                }
            } else
                hs.append(c);
        }
        return hs.toString();
    }

    public static char getChar() {
        return source[(int) (Math.random() * source.length)];
    }

    public static String getString(int length) {
        char[] strs = new char[length];
        for (int i = 0, end = length; i < end; i++) {
            strs[i] = getChar();
        }
        return new String(strs);
    }

    public static String getStrings(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, end = length; i < end; i++) {
            int j = (int) (Math.random() * 26);
            char a = (char) ('a' + j);
            sb.append(a);
        }
        return sb.toString();
    }

    public static String getS(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, end = length; i < end; i++) {
            sb.append(test.charAt((int) (Math.random() * test.length())));
        }
        return sb.toString();
    }

    public static String getSs() {
        for (int i = 0, end = source.length; i < end; i++) {
            int a = (int) (Math.random() * source.length);
            int b = (int) (Math.random() * source.length);
            char temp = source[a];
            source[a] = source[b];
            source[b] = temp;
        }
        return new String(source);
    }

    public static void swap(char[] v, int j, int i) {
        char temp = v[j];
        v[j] = v[i];
        v[i] = temp;
    }

    public static String sort1(char[] s) {
        for (int i = 0, end = s.length; i < end; i++) {
            for (int j = 0, end2 = s.length; j < end2 - i - 1; j++) {
                if (s[j] > s[j + 1]) {
                    swap(s, j, j + 1);
                }
            }
        }
        return new String(s);
    }

    public static void getShui() {
        for (int i = 100, end = 1000; i < end; i++) {
            int x = i / 100;
            int y = (i - x * 100) / 10;
            int z = i % 10;

            if (i == (x * x * x + y * y * y + z * z * z)) {
                System.out.println(i);
            }
        }
    }
}
