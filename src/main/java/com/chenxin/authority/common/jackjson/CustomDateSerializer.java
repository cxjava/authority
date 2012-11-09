package com.chenxin.authority.common.jackjson;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * 
 * java日期对象经过Jackson库转换成JSON日期格式化自定义类
 * 
 * <pre>
 * &#064;JsonSerialize(using = CustomDateSerializer.class)
 * public Date getTime() {
 * 	this.time = time;
 * }
 * </pre>
 * 
 * @author Maty Chen
 * @date 2010-6-20 下午02:50:54
 */
public class CustomDateSerializer extends JsonSerializer<Date> {
	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(DateFormatUtils.format(value, "yyyy-MM-dd"));
	}
}