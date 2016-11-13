package com.chenxin.authority.util;

/**
 * web中使用的常量
 *
 * @author Maty Chen
 * @date 2011-6-16 下午01:55:19
 */
public interface WebConstants {
    /**
     * 超时提醒
     */
    static final String TIME_OUT = "{\"error\":true,\"msg\":\"登录超时,请重新登录！\"}";
    /**
     * 保存session中的admin用户key
     */
    static final String CURRENT_USER = "CURRENT_USER";
    /**
     * exception
     */
    static final String JSON_MAPPING_EXCEPTION = "JsonMappingException: ";
    /**
     * exception
     */
    static final String IO_EXCEPTION = "IOException: ";
    /**
     * exception
     */
    static final String JSON_GENERATION_EXCEPTION = "JsonGenerationException: ";
    /**
     * exception
     */
    static final String EXCEPTION = "Exception: ";
    String IPLIST = "113.57.187.29|61.147.122.47";
}
