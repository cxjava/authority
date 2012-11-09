package com.chenxin.authority.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.chenxin.authority.pojo.BaseModule;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.Combo;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.Tree;

public interface BaseModuleService {

	Page<BaseModule> selectByParameters(ExtPager pager, Map<String, Object> parameters);

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
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String saveModule(Criteria example);

	/**
	 * 保存系统菜单
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String saveModules(Criteria example);

	/**
	 * 删除系统菜单
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String delete(Criteria example);

	/**
	 * 动态sql<br>
	 * 最好不要带外部参数拼装进来，防止SQL注入<br>
	 * 非正常情况不要用
	 * 
	 * @param example
	 * @return
	 */
	List<HashMap<String, Object>> selectByDynamicSql(Criteria example);

	/**
	 * 查找EXTjs里面的Combo对象
	 * 
	 * @param example
	 * @return
	 */
	List<Combo> selectComboBySql(Criteria example);

	/**
	 * 查找EXTjs里面的Combo对象
	 * 
	 * @param example
	 * @return
	 */
	Map<Object, Object> selectComboBySql(String sql);
}