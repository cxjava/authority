package com.chenxin.authority.service;

import com.chenxin.authority.pojo.BaseField;
import com.chenxin.authority.pojo.ExtPager;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface BaseFieldService {

	Page<BaseField> selectByParameters(ExtPager pager, Map<String, Object> parameters);

	HashMap<String, String> selectAll();

	/**
	 * 保存系统字段设置
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	BaseField saveField(BaseField field);

	/**
	 * 删除系统字段设置
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	void delete(Long fieldId);
}