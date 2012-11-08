package com.chenxin.authority.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chenxin.authority.common.jackjson.Jackson;
import com.chenxin.authority.common.springmvc.DateConvertEditor;
import com.chenxin.authority.pojo.BaseModule;
import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.pojo.Tree;
import com.chenxin.authority.service.BaseModuleService;
import com.chenxin.authority.service.BaseRoleModuleService;

/**
 * 后台资源、系统菜单相关
 * 
 * @author chenxin
 * @date 2011-10-31 下午10:16:24
 */
@Controller
@RequestMapping("/module")
public class ModuleController {

	private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);
	@Autowired
	private BaseModuleService baseModulesService;
	@Autowired
	private BaseRoleModuleService baseRoleModuleService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * index
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String module(Model model) {
		// 先查出所有的父节点
		StringBuilder sb = new StringBuilder();
		sb.append("select a.module_id   as k, ").append("       a.module_name as v ").append("from   base_modules a ")
				.append("where  a.leaf = 0 ").append("order by a.module_id asc ");
		Map<Object, Object> map = this.baseModulesService.selectComboBySql(sb.toString());
		map.put("0", "主菜单");
		model.addAttribute("moduleMap", Jackson.objToJson(map));
		return "user/module";
	}

	/**
	 * 查找系统的所有菜单
	 * 
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void all(PrintWriter writer) throws IOException {
		Criteria criteria = new Criteria();
		Tree tree = this.baseModulesService.selectAllModules(criteria);
		String json = Jackson.objToJson(tree.getChildren());
		// 加入check
		writer.write(json.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
		writer.flush();
		writer.close();
	}

	/**
	 * 所有菜单信息
	 */
	@RequestMapping("/all")
	@ResponseBody
	public Object all(ExtPager pager, @RequestParam(required = false) String moduleName) {
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
				criteria.setOrderByClause(" PARENT_ID asc,DISPLAY_INDEX asc ");
			}
			if (StringUtils.isNotBlank(moduleName)) {
				criteria.put("moduleNameLike", moduleName);
			}
			List<BaseModule> list = this.baseModulesService.selectByExample(criteria);
			int total = this.baseModulesService.countByExample(criteria);
			logger.debug("total:{}", total);
			return new ExtGridReturn(total, list);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 加载此角色的所有资源
	 */
	@RequestMapping("/{roleId}")
	@ResponseBody
	public Object selectModulesByRoleId(@PathVariable String roleId) {
		try {
			if (StringUtils.isBlank(roleId)) {
				return new ExtReturn(false, "角色ID不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("roleId", roleId);
			List<BaseRoleModule> list = this.baseRoleModuleService.selectByExample(criteria);
			return list;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 保存角色的系统菜单信息
	 */
	@RequestMapping("/saverole")
	@ResponseBody
	public Object save(@RequestParam String roleId, @RequestParam String moduleIds) {
		try {
			ArrayList<Integer> modulesIdList = new ArrayList<Integer>();
			if (StringUtils.isBlank(roleId)) {
				return new ExtReturn(false, "角色不能为空！");
			}
			if (StringUtils.isBlank(moduleIds)) {
				return new ExtReturn(false, "选择的资源不能为空！");
			} else {
				String[] modules = StringUtils.split(moduleIds, ",");
				if (null == modules || modules.length == 0) {
					return new ExtReturn(false, "选择的资源不能为空！");
				}
				for (int i = 0; i < modules.length; i++) {
					modulesIdList.add(new Integer(modules[i]));
				}
			}
			logger.debug("save() - String roleId={}", roleId);
			logger.debug("save() - String moduleIds={}", moduleIds);
			Criteria criteria = new Criteria();
			criteria.put("modulesIdList", modulesIdList);
			criteria.put("roleId", roleId);
			String result = this.baseModulesService.saveModule(criteria);
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
	 * 保存系统菜单信息
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(BaseModule modules) {
		try {
			if (modules == null) {
				return new ExtReturn(false, "模块不能为空！");
			}
			if (StringUtils.isBlank(modules.getModuleName())) {
				return new ExtReturn(false, "模块名称不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("modules", modules);
			String result = this.baseModulesService.saveModules(criteria);
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
	 * 删除该模块
	 */
	@RequestMapping("/del/{moduleId}")
	@ResponseBody
	public Object delete(@PathVariable String moduleId) {
		try {
			if (StringUtils.isBlank(moduleId)) {
				return new ExtReturn(false, "模块主键不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("moduleId", moduleId);
			String result = this.baseModulesService.delete(criteria);
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
