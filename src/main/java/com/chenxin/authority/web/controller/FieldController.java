package com.chenxin.authority.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chenxin.authority.common.springmvc.DateConvertEditor;
import com.chenxin.authority.pojo.BaseField;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.service.BaseFieldService;
import com.chenxin.authority.service.ServiceException;
import com.google.common.collect.Maps;

/**
 * 系统字段设置
 * 
 * @author Maty Chen
 * @date 2011-12-19 下午1:48:19
 */
@Controller
@RequestMapping("/field")
public class FieldController {
	private static final Logger logger = LoggerFactory.getLogger(FieldController.class);

	@Autowired
	private BaseFieldService baseFieldsService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * index
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "user/field";
	}

	/**
	 * 所有系统字段
	 */
	@RequestMapping("/all")
	@ResponseBody
	public Object all(ExtPager pager, @RequestParam(required = false) String fieldName) {
		try {
			Map<String, Object> parameters = Maps.newHashMap();
			if (StringUtils.isNotBlank(fieldName)) {
				parameters.put("LIKE_fieldName", fieldName);
			}
			Page<BaseField> page = this.baseFieldsService.getBaseField(pager, parameters);
			return new ExtGridReturn(page.getTotalElements(), page.getContent());
		} catch (ServiceException e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(BaseField fields) {
		try {
			if (fields == null) {
				return new ExtReturn(false, "系统字段不能为空！");
			}
			if (StringUtils.isBlank(fields.getField())) {
				return new ExtReturn(false, "字段不能为空！");
			}
			if (StringUtils.isBlank(fields.getFieldName())) {
				return new ExtReturn(false, "字段名称不能为空！");
			}
			if (StringUtils.isBlank(fields.getValueField())) {
				return new ExtReturn(false, "字段值不能为空！");
			}
			if (StringUtils.isBlank(fields.getDisplayField())) {
				return new ExtReturn(false, "字段显示值不能为空！");
			}
			this.baseFieldsService.saveField(fields);
			return new ExtReturn(true, "保存成功！");
		} catch (ServiceException e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("/del/{fieldId}")
	@ResponseBody
	public Object delete(@PathVariable Long fieldId) {
		try {
			if (fieldId == null) {
				return new ExtReturn(false, "主键不能为空！");
			}
			this.baseFieldsService.delete(fieldId);
			return new ExtReturn(true, "删除成功！");
		} catch (ServiceException e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 内存同步
	 */
	@RequestMapping("/synchro")
	@ResponseBody
	public Object synchro(HttpSession session) {
		try {
			session.getServletContext().removeAttribute("fields");
			session.getServletContext().setAttribute("fields", baseFieldsService.selectAll());
			return new ExtReturn(true, "同步成功！");
		} catch (ServiceException e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
