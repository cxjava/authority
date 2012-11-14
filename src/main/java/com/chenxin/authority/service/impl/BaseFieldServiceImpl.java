package com.chenxin.authority.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxin.authority.common.jackjson.Jackson;
import com.chenxin.authority.common.utils.JpaTools;
import com.chenxin.authority.dao.BaseFieldRepository;
import com.chenxin.authority.pojo.BaseField;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.service.BaseFieldService;
import com.google.common.collect.Maps;

@Service
@Transactional(readOnly = true)
public class BaseFieldServiceImpl implements BaseFieldService {
	@Autowired
	private BaseFieldRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(BaseFieldServiceImpl.class);

	@Override
	public Map<String, String> selectAll() {
		logger.info("Start reading system default configuration");
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("enabled", 1);
		Specification<BaseField> spec = JpaTools.getSpecification(parameters, BaseField.class);
		Sort sort = JpaTools.getSort(null, " field desc ,sort asc ");
		List<BaseField> list = this.repository.findAll(spec, sort);

		HashMap<String, LinkedHashMap<String, String>> all = Maps.newHashMap();
		LinkedHashMap<String, String> part = null;
		for (int i = 0; i < list.size(); i++) {
			BaseField baseFields = list.get(i);
			String key = baseFields.getField();
			if (all.containsKey(key)) {
				// 如果包含这个field，就加入它的值
				part = all.get(key);
				part.put(baseFields.getValueField(), baseFields.getDisplayField());
			} else {
				part = Maps.newLinkedHashMap();
				part.put(baseFields.getValueField(), baseFields.getDisplayField());
				// 没有这个fiel，则新加入这个filed
				all.put(key, part);
			}
		}
		part = Maps.newLinkedHashMap();
		for (Entry<String, LinkedHashMap<String, String>> entry : all.entrySet()) {
			String key = entry.getKey();
			HashMap<String, String> value = entry.getValue();
			// 为了eval('(${applicationScope.fields.sex})')这个单引号使用,替换所有的'，为\'
			String val = StringEscapeUtils.escapeEcmaScript(Jackson.objToJson(value));
			logger.debug(val);
			part.put(key, val);
		}
		logger.info("The end of the reading system default configuration");
		return part;
	}

	@Override
	@Transactional
	public BaseField saveField(BaseField field) {
		return this.repository.save(field);
	}

	@Override
	@Transactional
	public void delete(Long fieldId) {
		this.repository.delete(fieldId);
	}

	@Override
	public Page<BaseField> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
		PageRequest pageable = JpaTools.getPageRequest(pager, " field desc ,sort asc ");
		Specification<BaseField> spec = JpaTools.getSpecification(parameters, BaseField.class);
		return this.repository.findAll(spec, pageable);
	}
}