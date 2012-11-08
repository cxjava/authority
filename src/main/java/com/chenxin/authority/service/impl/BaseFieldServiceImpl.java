package com.chenxin.authority.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chenxin.authority.common.jackjson.Jackson;
import com.chenxin.authority.dao.BaseFieldRepository;
import com.chenxin.authority.pojo.BaseField;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.service.BaseFieldService;

@Service
public class BaseFieldServiceImpl implements BaseFieldService {
	@Autowired
	private BaseFieldRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(BaseFieldServiceImpl.class);

	@Override
	public int countByExample(Criteria example) {
		int count = this.repository.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseField selectByPrimaryKey(String fieldId) {
		return this.repository.selectByPrimaryKey(fieldId);
	}

	@Override
	public List<BaseField> selectByExample(Criteria example) {
		return this.repository.selectByExample(example);
	}

	@Override
	public HashMap<String, String> selectAllByExample(Criteria example) {
		logger.info("开始读取系统默认配置");
		List<BaseField> list = this.repository.selectByExample(example);
		HashMap<String, LinkedHashMap<String, String>> all = new HashMap<String, LinkedHashMap<String, String>>();
		LinkedHashMap<String, String> part = null;
		for (int i = 0; i < list.size(); i++) {
			BaseField baseFields = list.get(i);
			String key = baseFields.getField();
			if (all.containsKey(key)) {
				// 如果包含这个field，就加入它的值
				part = all.get(key);
				part.put(baseFields.getValueField(), baseFields.getDisplayField());
			} else {
				part = new LinkedHashMap<String, String>();
				part.put(baseFields.getValueField(), baseFields.getDisplayField());
				// 没有这个fiel，则新加入这个filed
				all.put(key, part);
			}
		}
		part = new LinkedHashMap<String, String>();
		for (Entry<String, LinkedHashMap<String, String>> entry : all.entrySet()) {
			String key = entry.getKey();
			HashMap<String, String> value = entry.getValue();
			// 为了eval('(${applicationScope.fields.sex})')这个单引号使用,替换所有的'，为\'
			//String val = Jackson.objToJson(value).replaceAll("\\'", "\\\\'");
			String val = StringEscapeUtils.escapeEcmaScript(Jackson.objToJson(value));
			logger.info(val);
			part.put(key, val);
		}
		logger.info("结束读取系统默认配置");
		return part;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveFields(Criteria example) {
		BaseField fields = (BaseField) example.get("fields");

		int result = 0;
		if (fields.getId() == null) {
			result = this.repository.save(fields);
		} else {
			result = this.repository.save(fields);
		}
		return result > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String delete(Criteria example) {
		String fieldId = example.getString("fieldId");
		int result = 0;
		// 删除自己
		result = this.repository.deleteByPrimaryKey(fieldId);
		return result > 0 ? "01" : "00";
	}

}