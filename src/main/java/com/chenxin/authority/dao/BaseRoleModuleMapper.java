package com.chenxin.authority.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.Criteria;

public interface BaseRoleModuleMapper {
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
	int deleteByPrimaryKey(String roleModuleId);

	/**
	 * 保存记录,不管记录里面的属性是否为空
	 */
	int insert(BaseRoleModule record);

	/**
	 * 保存属性不为空的记录
	 */
	int insertSelective(BaseRoleModule record);

	/**
	 * 根据条件查询记录集
	 */
	List<BaseRoleModule> selectByExample(Criteria example);

	/**
	 * 根据主键查询记录
	 */
	BaseRoleModule selectByPrimaryKey(String roleModuleId);

	/**
	 * 根据条件更新属性不为空的记录
	 */
	int updateByExampleSelective(@Param("record") BaseRoleModule record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据条件更新记录
	 */
	int updateByExample(@Param("record") BaseRoleModule record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据主键更新属性不为空的记录
	 */
	int updateByPrimaryKeySelective(BaseRoleModule record);

	/**
	 * 根据主键更新记录
	 */
	int updateByPrimaryKey(BaseRoleModule record);
}