package com.chenxin.authority.service;

import com.alibaba.fastjson.JSON;
import com.chenxin.authority.dao.BaseFieldRepository;
import com.chenxin.authority.entity.BaseField;
import com.chenxin.authority.entity.ExtPager;
import com.chenxin.authority.util.JpaTools;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
@Transactional
public class BaseFieldService {
    @Autowired
    private BaseFieldRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(BaseFieldService.class);

    public HashMap<String, LinkedHashMap<String, String>> selectAllAsMap() {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("enabled", 1);
        Specification<BaseField> spec = JpaTools.getSpecification(parameters, BaseField.class);
        Sort sort = JpaTools.getSort(null, " field desc ,sort asc ");
        List<BaseField> list = this.repository.findAll(spec, sort);

        HashMap<String, LinkedHashMap<String, String>> all = Maps.newHashMap();
        LinkedHashMap<String, String> jsonMap;
        for (BaseField dictionary : list) {
            String key = dictionary.getField();
            if (all.containsKey(key)) {
                // 如果包含这个field，就加入它的值
                jsonMap = all.get(key);
                jsonMap.put(dictionary.getValueField(), dictionary.getDisplayField());
            } else {
                jsonMap = Maps.newLinkedHashMap();
                jsonMap.put(dictionary.getValueField(), dictionary.getDisplayField());
                // 没有这个fiel，则新加入这个filed
                all.put(key, jsonMap);
            }
        }
        return all;
    }


    public Map<String, String> selectAll() {
        logger.info("读取系统字典表....");
        HashMap<String, LinkedHashMap<String, String>> all = selectAllAsMap();
        LinkedHashMap<String, String> jsonMap = Maps.newLinkedHashMap();

        for (Entry<String, LinkedHashMap<String, String>> entry : all.entrySet()) {
            String key = entry.getKey();
            HashMap<String, String> value = entry.getValue();
            // 为了eval('(${applicationScope.fields.sex})')这个单引号使用,替换所有的'，为\'
//            String val = StringEscapeUtils.escapeEcmaScript(JSON.toJSONString(value));
            String val = JSON.toJSONString(value);
            logger.debug("{}==>{}.", key, val);
            jsonMap.put(key, val);
        }
        logger.info("读取系统字典表成功!!");
        return jsonMap;
    }


    @Transactional
    public BaseField saveField(BaseField field) {
        return this.repository.save(field);
    }


    @Transactional
    public void delete(Long fieldId) {
        this.repository.delete(fieldId);
    }


    public Page<BaseField> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
        PageRequest pageable = JpaTools.getPageRequest(pager, " field desc ,sort asc ");
        Specification<BaseField> spec = JpaTools.getSpecification(parameters, BaseField.class);
        return this.repository.findAll(spec, pageable);
    }
}