package com.chenxin.authority.common.jackjson.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import com.chenxin.authority.common.utils.ClassLoaderUtil;
import com.chenxin.authority.common.utils.FileDigest;
import com.chenxin.authority.common.utils.PropertiesHolder;

/**
 * 
 * 
 * @author chenxin
 * @date 2011-5-12 下午02:31:26
 */
public class MainTest {
	public static void main(String[] args) throws Exception {
		
		String aa="aaa,";
		String [] bb=aa.split(",");
		System.out.println(bb.length);
		for (int i = 0, end = bb.length; i < end; i++) {
			System.out.println(bb[i]);
		}
		
		Map<String,String> map=MapUtils.EMPTY_MAP;
		MapUtils.isEmpty(map);
		
		File file=new File("");
		file.listFiles();
		FileUtils.cleanDirectory(file);
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private static void test1() throws JsonGenerationException, JsonMappingException, IOException {
		User user = new User();
		user.setAge(23);
		user.setName("cx");
		user.setPassword("123456");
		ObjectMapper mapper = new ObjectMapper();
		FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept("password"));
		// SimpleBeanPropertyFilter.filterOutAllExcept("password"));
		// and then serialize using that filter provider:
		String json = mapper.filteredWriter(filters).writeValueAsString(user);
		System.out.println(json);
		InputStreamReader reader = null;
		Properties properties = new Properties();
		InputStream is = ClassLoaderUtil.getResourceAsStream("config/others/config.properties", MainTest.class);
		if (null != is) {
			reader = new InputStreamReader(is, "UTF-8");
			properties.load(reader);
		}
		PropertiesHolder p = new PropertiesHolder();
		p.setProperties(properties);

//		test1();DigestUtils.md5Hex(DigestUtils.md5Hex(password){account})
		String str = "";
		System.out.println(DigestUtils.md5Hex("123456"));
		System.out.println(DigestUtils.shaHex(str));
		System.out.println(DigestUtils.sha256Hex(str));
		System.out.println(DigestUtils.sha384Hex(str));
		System.out.println(DigestUtils.sha512Hex(str));
		
		System.out.println(FileDigest.getFileMD5(new File("D:/apache/tomcat-6.0.35/logs/lottery2.0/all.2011-12-31.txt")));
		System.out.println(FileDigest.getFileMD5(new File("D:/apache/tomcat-6.0.35/logs/lottery2.0/all.2011-12-30.txt")));
		System.out.println(FileDigest.getFileMD5(new File("D:/apache/tomcat-6.0.35/logs/lottery2.0/all.2011-12-29.txt")));
		long start=System.currentTimeMillis();
		System.out.println("耗时(毫秒)："+(System.currentTimeMillis()-start));
		Map<String, String> maps=FileDigest.getDirMD5(new File("D:/apache/tomcat-6.0.35/logs"), true);
		for (Entry<String, String> entry : maps.entrySet()) {
			String key = entry.getKey();
			String md5 = entry.getValue();
			System.out.println(md5+" "+key);
		}
		
	}
}
