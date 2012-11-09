package com.chenxin.authority.web.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.chenxin.authority.pojo.BaseRole;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.service.BaseRoleService;
import com.google.common.collect.Maps;

/**
 * 角色
 * 
 * @author Maty Chen
 * @date 2011-10-21 下午2:49:31
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	@Autowired
	private BaseRoleService baseRolesService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * index
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String role() {
		return "user/role";
	}

	/**
	 * 查找所有的角色
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object all(ExtPager pager, @RequestParam(required = false) String roleName) {
		Map<String, Object> parameters = Maps.newHashMap();
		if (StringUtils.isNotBlank(roleName)) {
			parameters.put("LIKE_roleName", roleName);
		}
		Page<BaseRole> page = this.baseRolesService.selectByParameters(pager, parameters);
		return new ExtGridReturn(page.getTotalElements(), page.getContent());
	}

	/**
	 * 保存角色信息
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(BaseRole role) {
		try {
			if (role == null) {
				return new ExtReturn(false, "角色不能为空!");
			}
			if (StringUtils.isBlank(role.getRoleName())) {
				return new ExtReturn(false, "角色名称不能为空!");
			}
			this.baseRolesService.saveRole(role);
			return new ExtReturn(true, "保存成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 删除该角色
	 */
	@RequestMapping("/del/{roleId}")
	@ResponseBody
	public Object delete(@PathVariable Long roleId) {
		try {
			if (null == roleId) {
				return new ExtReturn(false, "角色主键不能为空！");
			}
			this.baseRolesService.deleteByPrimaryKey(roleId);
			return new ExtReturn(true, "删除成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
