package com.chenxin.authority.common.jackjson.test;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * 
 * 
 * @author Maty Chen
 * @date 2011-5-12 下午02:30:10
 */
@JsonFilter("myFilter")
public class User {

	private String name;
	private String password;
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
