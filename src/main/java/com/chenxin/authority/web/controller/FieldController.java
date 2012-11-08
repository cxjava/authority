package com.chenxin.authority.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.service.BaseFieldService;

/**
 * 系统字段设置
 * 
 * @author chenxin
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
			Criteria criteria = new Criteria();
			// 设置分页信息
			if (pager.getLimit() != null && pager.getStart() != null) {
				criteria.setOracleEnd(pager.getLimit() + pager.getStart());
				criteria.setOracleStart(pager.getStart());
			}
			// 排序信息
			if (StringUtils.isNotBlank(pager.getDir()) && StringUtils.isNotBlank(pager.getSort())) {
				criteria.setOrderByClause(pager.getSort() + " " + pager.getDir());
			} else {
				criteria.setOrderByClause(" field desc ,sort asc ");
			}
			if (StringUtils.isNotBlank(fieldName)) {
				criteria.put("fieldNameLike", fieldName);
			}
			List<BaseField> list = this.baseFieldsService.selectByExample(criteria);
			int total = this.baseFieldsService.countByExample(criteria);
			logger.debug("total:{}", total);
			return new ExtGridReturn(total, list);
		} catch (Exception e) {
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
			Criteria criteria = new Criteria();
			criteria.put("fields", fields);
			String result = this.baseFieldsService.saveFields(criteria);
			if ("01".equals(result)) {
				return new ExtReturn(true, "保存成功！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "保存失败！");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("/del/{fieldId}")
	@ResponseBody
	public Object delete(@PathVariable String fieldId) {
		try {
			if (StringUtils.isBlank(fieldId)) {
				return new ExtReturn(false, "主键不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("fieldId", fieldId);
			String result = this.baseFieldsService.delete(criteria);
			if ("01".equals(result)) {
				return new ExtReturn(true, "删除成功！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "删除失败！");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
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
			Criteria criteria = new Criteria();
			criteria.setOrderByClause(" field desc ,sort asc ");
			criteria.put("enabled", "1");
			session.getServletContext().removeAttribute("fields");
			session.getServletContext().setAttribute("fields", baseFieldsService.selectAllByExample(criteria));
			return new ExtReturn(true, "同步成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
