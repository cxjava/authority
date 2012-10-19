package com.chenxin.authority.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.chenxin.authority.pojo.BaseRoles;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.service.BaseRolesService;

/**
 * 角色
 * 
 * @author chenxin
 * @date 2011-10-21 下午2:49:31
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	@Autowired
	private BaseRolesService baseRolesService;

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
		Criteria criteria = new Criteria();
		// 设置分页信息
		if (pager.getLimit() != null && pager.getStart() != null) {
			criteria.setOracleEnd(pager.getLimit() + pager.getStart());
			criteria.setOracleStart(pager.getStart());
		}
		// 排序信息
		if (StringUtils.isNotBlank(pager.getDir()) && StringUtils.isNotBlank(pager.getSort())) {
			criteria.setOrderByClause(pager.getSort() + " " + pager.getDir());
		}
		if (StringUtils.isNotBlank(roleName)) {
			criteria.put("roleNameLike", roleName);
		}
		List<BaseRoles> list = this.baseRolesService.selectByExample(criteria);
		int total = this.baseRolesService.countByExample(criteria);
		logger.debug("total:{}", total);
		return new ExtGridReturn(total, list);
	}

	/**
	 * 保存角色信息
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(BaseRoles role) {
		try {
			if (role == null) {
				return new ExtReturn(false, "角色不能为空!");
			}
			if (StringUtils.isBlank(role.getRoleName())) {
				return new ExtReturn(false, "角色名称不能为空!");
			}
			String result = this.baseRolesService.saveRole(role);
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
	 * 删除该角色
	 */
	@RequestMapping("/del/{roleId}")
	@ResponseBody
	public Object delete(@PathVariable String roleId) {
		try {
			if (StringUtils.isBlank(roleId)) {
				return new ExtReturn(false, "角色主键不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("roleId", roleId);
			String result = this.baseRolesService.deleteByPrimaryKey(criteria);
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
}
