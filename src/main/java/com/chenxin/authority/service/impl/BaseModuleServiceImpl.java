package com.chenxin.authority.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxin.authority.common.utils.JpaTools;
import com.chenxin.authority.dao.BaseModuleRepository;
import com.chenxin.authority.dao.BaseRoleModuleRepository;
import com.chenxin.authority.dao.BaseUserRepository;
import com.chenxin.authority.pojo.BaseModule;
import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.Tree;
import com.chenxin.authority.pojo.TreeMenu;
import com.chenxin.authority.service.BaseModuleService;
import com.google.common.collect.Maps;

@Service
public class BaseModuleServiceImpl implements BaseModuleService {
	@Autowired
	private BaseUserRepository baseUserRepository;
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
	public Tree selectAllModules() {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("Distinct", "");
		if (!isDisplay) {
			// 是否显示 0:否 1:是
			// 这个条件表示只显示允许显示的模块，否则没有这个条件会显示所有的模块
			parameters.put("isDisplay", 1);
		}
		Specification<BaseModule> spec = JpaTools.getSpecification(parameters, BaseModule.class);
		Sort sort = JpaTools.getSort(null, " displayIndex ASC ");
		List<BaseModule> list = this.baseModulesRepository.findAll(spec, sort);
		logger.info("list.size():{}",list.size());
		TreeMenu menu = new TreeMenu(list);
		return menu.getTreeJson();
	}

	@Override
	public Tree selectModulesByUser(BaseUser baseUser) {
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("Distinct", "");
		if (!isDisplay) {
			// 是否显示 0:否 1:是
			// 这个条件表示只显示允许显示的模块，否则没有这个条件会显示所有的模块
			parameters.put("isDisplay", 1);
		}
		List<BaseModule> list = null;
		// 显示所有模块
//		if (resoved) {
			Sort sort = JpaTools.getSort(null, " displayIndex ASC ");
			Specification<BaseModule> spec = JpaTools.getSpecification(parameters, BaseModule.class);
			list = this.baseModulesRepository.findAll(spec, sort);
			logger.info("list.size():{}",list.size());
//		} else {
//			// 显示当前用户权限模块，从配置表中获取
//			parameters = Maps.newHashMap();
//			parameters.put("Distinct", "");
//			Sort sort = JpaTools.getSort(null, " displayIndex ASC ");
//			BaseUser user = this.baseUserRepository.findOne(baseUser.getId());
//		}
		TreeMenu menu = new TreeMenu(list);
		return menu.getTreeJson();
	}
	@Override
	@Transactional
	public String saveModule(Long roleId,ArrayList<Long> modulesIdList) {
		// 删除以前的资源
		this.baseRoleModuleRepository.deleteByRoleId(roleId);
		for (Long moduleId : modulesIdList) {
			if (moduleId != null) {
				BaseRoleModule roleModule = new BaseRoleModule();
				roleModule.setModuleId(moduleId);
				roleModule.setRoleId(roleId);
				this.baseRoleModuleRepository.save(roleModule);
			}
		}
		return "01";
	}

	@Override
	public void saveModules(BaseModule modules) {
			this.baseModulesRepository.save(modules);
	}

	@Override
	@Transactional
	public void delete(Long moduleId) {
		// 删除这个模块下面的菜单
		this.baseModulesRepository.deleteByParentUrl(moduleId);
		// 删除自己
		this.baseModulesRepository.delete(moduleId);
	}

	@Override
	public Page<BaseModule> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
		PageRequest pageable = JpaTools.getPageRequest(pager, "");
		Specification<BaseModule> spec = JpaTools.getSpecification(parameters, BaseModule.class);
		return this.baseModulesRepository.findAll(spec, pageable);
	}
	@Override
	public List<BaseRoleModule> selectModuleByRoleId(Long roleId) {
		return this.baseRoleModuleRepository.findByRoleId(roleId);
	}

	@Override
	public Map<String, Object> selectParentModule() {
		List<BaseModule> list=this.baseModulesRepository.findByLeaf(0);
		 Map<String, Object> maps=Maps.newHashMap();
		 for(BaseModule base:list){
			 maps.put(base.getId().toString(), base.getModuleName());
		 }
		return maps;
	}

}