package com.chenxin.authority.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.chenxin.authority.pojo.BaseModule;
import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.Tree;

public interface BaseModuleService {

	Page<BaseModule> selectByParameters(ExtPager pager, Map<String, Object> parameters);

	List<BaseRoleModule> selectModuleByRoleId(Long roleId);

	/**
	 * 查找用户的模块
	 * 
	 * @param baseUser
	 * @return 00：失败，01：成功 ,其他情况
	 */
	Tree selectModulesByUser(BaseUser baseUser);

	/**
	 * 查找用户的模块
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	Tree selectAllModules();

	/**
	 * 保存角色的系统菜单
	 * 
	 */
	String saveModule(Long roleId, List<Long> modulesIdList);

	/**
	 * 保存系统菜单
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	void saveModules(BaseModule example);

	/**
	 * 删除系统菜单
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	void delete(Long moduleId);

	Map<String, Object> selectParentModule();
}