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
import org.springframework.data.domain.Page;
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
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.pojo.Tree;
import com.chenxin.authority.service.BaseModuleService;
import com.google.common.collect.Maps;

/**
 * 后台资源、系统菜单相关
 * 
 * @author Maty Chen
 * @date 2011-10-31 下午10:16:24
 */
@Controller
@RequestMapping("/module")
public class ModuleController {

	private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);
	@Autowired
	private BaseModuleService baseModulesService;

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
		Tree tree = this.baseModulesService.selectAllModules();
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
			Map<String, Object> parameters = Maps.newHashMap();
			if (StringUtils.isNotBlank(moduleName)) {
				parameters.put("LIKE_moduleName", moduleName);
			}
			Page<BaseModule> page = this.baseModulesService.selectByParameters(pager, parameters);
			return new ExtGridReturn(page.getTotalElements(), page.getContent());
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
	public Object selectModulesByRoleId(@PathVariable Long roleId) {
		try {
			if (null == roleId) {
				return new ExtReturn(false, "角色ID不能为空！");
			}
			List<BaseRoleModule> list = this.baseModulesService.selectModuleByRoleId(roleId);
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
	public Object save(@RequestParam Long roleId, @RequestParam String moduleIds) {
		try {
			ArrayList<Long> modulesIdList = new ArrayList<Long>();
			if (null==roleId) {
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
					modulesIdList.add(new Long(modules[i]));
				}
			}
			logger.debug("save() - String roleId={}", roleId);
			logger.debug("save() - String moduleIds={}", moduleIds);
			String result = this.baseModulesService.saveModule(roleId,modulesIdList);
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
			this.baseModulesService.saveModules(modules);
			return new ExtReturn(true, "保存成功！");
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
	public Object delete(@PathVariable Long moduleId) {
		try {
			if (null==moduleId) {
				return new ExtReturn(false, "模块主键不能为空！");
			}
			this.baseModulesService.delete(moduleId);
			return new ExtReturn(true, "删除成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}
}
