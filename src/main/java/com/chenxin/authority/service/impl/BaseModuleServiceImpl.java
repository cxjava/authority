package com.chenxin.authority.service.impl;

import com.chenxin.authority.dao.BaseModuleRepository;
import com.chenxin.authority.dao.BaseRoleModuleRepository;
import com.chenxin.authority.pojo.BaseModule;
import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.Combo;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.Tree;
import com.chenxin.authority.pojo.TreeMenu;
import com.chenxin.authority.service.BaseModuleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BaseModuleServiceImpl implements BaseModuleService {
	@Autowired
	private BaseModuleRepository baseModulesRepository;
	@Autowired
	private BaseRoleModuleRepository baseRoleModuleRepository;
	/**
	 * 开发阶段是否显示全部模块
	 */
	@Value("${resoved:false}")
	private boolean resoved;
	/**
	 * 是否需要显示被隐藏的模块,true显示被隐藏的模块，false表示不显示隐藏的模块
	 */
	@Value("${isDisplay:false}")
	private boolean isDisplay;

	private static final Logger logger = LoggerFactory.getLogger(BaseModuleServiceImpl.class);

	@Override
	public Tree selectAllModules(Criteria example) {
		example.setDistinct(true);
		example.setOrderByClause(" DISPLAY_INDEX ASC");
		if (!isDisplay) {
			// 是否显示 0:否 1:是
			// 这个条件表示只显示允许显示的模块，否则没有这个条件会显示所有的模块
			example.put("isDisplay", 1);
		}
		List<BaseModule> list = this.baseModulesRepository.selectByExample(example);
		TreeMenu menu = new TreeMenu(list);
		return menu.getTreeJson();
	}

	@Override
	public Tree selectModulesByUser(BaseUser baseUser) {
		Criteria example = new Criteria();
		example.setDistinct(true);
		if (!isDisplay) {
			// 是否显示 0:否 1:是
			// 这个条件表示只显示允许显示的模块，否则没有这个条件会显示所有的模块
			example.put("isDisplay", 1);
		}
		List<BaseModule> list = null;
		// 显示所有模块
		if (resoved) {
			example.setOrderByClause(" DISPLAY_INDEX ASC");
			list = this.baseModulesRepository.selectByExample(example);
		} else {
			// 显示当前用户权限模块，从配置表中获取
			example.setOrderByClause(" a.display_index ASC");
			example.put("userId", baseUser.getId());
			list = this.baseModulesRepository.selectMyModules(example);
		}
		TreeMenu menu = new TreeMenu(list);
		return menu.getTreeJson();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveModule(Criteria criteria) {
		String roleId = criteria.getString("roleId");
		ArrayList<Integer> modulesIds = (ArrayList<Integer>) criteria.get("modulesIdList");
		// 删除以前的资源
		this.baseRoleModuleRepository.deleteByExample(criteria);
		for (Integer moduleId : modulesIds) {
			if (moduleId != null) {
				BaseRoleModule roleModule = new BaseRoleModule();
				roleModule.setModuleId(moduleId);
				roleModule.setRoleId(roleId);
				this.baseRoleModuleRepository.insert(roleModule);
			}
		}
		return "01";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveModules(Criteria example) {
		BaseModule modules = (BaseModule) example.get("modules");
		int result = 0;
		if (modules.getModuleId() == null) {
			result = this.baseModulesRepository.insertSelective(modules);
		} else {
			result = this.baseModulesRepository.updateByPrimaryKeySelective(modules);
		}
		return result > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String delete(Criteria example) {
		Integer moduleId = example.getInteger("moduleId");
		int result = 0;
		// 删除这个模块下面的菜单
		example.clear();
		example.put("parentId", moduleId);
		this.baseModulesRepository.deleteByExample(example);
		// 删除自己
		result = this.baseModulesRepository.deleteByPrimaryKey(moduleId);
		return result > 0 ? "01" : "00";
	}

	@Override
	public int countByExample(Criteria example) {
		int count = this.baseModulesRepository.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseModule selectByPrimaryKey(Integer moduleId) {
		return this.baseModulesRepository.selectByPrimaryKey(moduleId);
	}

	@Override
	public List<BaseModule> selectByExample(Criteria example) {
		return this.baseModulesRepository.selectByExample(example);
	}

	@Override
	public List<HashMap<String, Object>> selectByDynamicSql(Criteria example) {
		return this.baseModulesRepository.selectByDynamicSql(example);
	}

	@Override
	public List<Combo> selectComboBySql(Criteria example) {
		return this.baseModulesRepository.selectComboBySql(example);
	}

	@Override
	public Map<Object, Object> selectComboBySql(String sql) {
		Criteria criteria = new Criteria();
		criteria.put("comboSql", sql);
		List<Combo> list = this.baseModulesRepository.selectComboBySql(criteria);
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		for (Combo combo : list) {
			map.put(combo.getK(), combo.getV());
		}
		return map;
	}

}