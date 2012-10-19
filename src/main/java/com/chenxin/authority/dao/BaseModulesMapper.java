package com.chenxin.authority.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chenxin.authority.pojo.BaseModules;
import com.chenxin.authority.pojo.Combo;
import com.chenxin.authority.pojo.Criteria;

public interface BaseModulesMapper {
	/**
	 * 动态sql<br>
	 * 最好不要带外部参数拼装进来，防止SQL注入<br>
	 * 非正常情况不要用
	 * 
	 * @param example
	 * @return
	 */
	List<HashMap<String, Object>> selectByDynamicSql(Criteria dynamicSql);
	
	/**
	 * 查找EXTjs里面的Combo对象
	 * 
	 * @param example
	 * @return
	 */
	List<Combo> selectComboBySql(Criteria example);

	/**
	 * 根据条件查询记录总数
	 */
	int countByExample(Criteria example);

	/**
	 * 根据条件删除记录
	 */
	int deleteByExample(Criteria example);

	/**
	 * 根据主键删除记录
	 */
	int deleteByPrimaryKey(Integer moduleId);

	/**
	 * 保存记录,不管记录里面的属性是否为空
	 */
	int insert(BaseModules record);

	/**
	 * 保存属性不为空的记录
	 */
	int insertSelective(BaseModules record);

	/**
	 * 根据条件查询记录集
	 */
	List<BaseModules> selectByExample(Criteria example);

	/**
	 * 根据条件查询记录集
	 */
	List<BaseModules> selectMyModules(Criteria example);

	/**
	 * 根据主键查询记录
	 */
	BaseModules selectByPrimaryKey(Integer moduleId);

	/**
	 * 根据条件更新属性不为空的记录
	 */
	int updateByExampleSelective(@Param("record") BaseModules record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据条件更新记录
	 */
	int updateByExample(@Param("record") BaseModules record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据主键更新属性不为空的记录
	 */
	int updateByPrimaryKeySelective(BaseModules record);

	/**
	 * 根据主键更新记录
	 */
	int updateByPrimaryKey(BaseModules record);
}