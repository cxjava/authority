package com.chenxin.authority.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 打为jar包后也能找到配置文件，并以流的方式读取。 InputStream is =
 * ClassLoaderUtil.getResourceAsStream("config/others/config.properties",
 * MainTest.class); if (null != is) { reader = new InputStreamReader(is,
 * "UTF-8"); }
 * 
 * @author Maty Chen
 * @date 2010-11-17 下午04:36:20
 */
public class ClassLoaderUtil {
	private static final Logger logger = LoggerFactory.getLogger(ClassLoaderUtil.class);

	protected ClassLoaderUtil(){}
	public static URL getResource(String resourceName, Class<?> callingClass) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
		if (url == null) {
			url = ClassLoaderUtil.class.getClassLoader().getResource(resourceName);
		}
		if (url == null) {
			ClassLoader cl = callingClass.getClassLoader();
			if (cl != null) {
				url = cl.getResource(resourceName);
			}
		}
		if ((url == null) && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
			return getResource('/' + resourceName, callingClass);
		}
		if (url != null) {
			logger.info("配置文件路径为= " + url.getPath());
		}
		return url;
	}

	public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
		URL url = getResource(resourceName, callingClass);
		try {
			return (url != null) ? url.openStream() : null;
		} catch (IOException e) {
			logger.error("配置文件" + resourceName + "没有找到! ", e);
			return null;
		}
	}

}