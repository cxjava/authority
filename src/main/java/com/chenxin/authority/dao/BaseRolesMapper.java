package com.chenxin.authority.dao;

import com.chenxin.authority.pojo.BaseRoles;
import com.chenxin.authority.pojo.Criteria;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseRolesMapper {
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
	int deleteByPrimaryKey(String roleId);

	/**
	 * 保存记录,不管记录里面的属性是否为空
	 */
	int insert(BaseRoles record);

	/**
	 * 保存属性不为空的记录
	 */
	int insertSelective(BaseRoles record);

	/**
	 * 根据条件查询记录集
	 */
	List<BaseRoles> selectByExample(Criteria example);

	/**
	 * 根据主键查询记录
	 */
	BaseRoles selectByPrimaryKey(String roleId);

	/**
	 * 根据条件更新属性不为空的记录
	 */
	int updateByExampleSelective(@Param("record") BaseRoles record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据条件更新记录
	 */
	int updateByExample(@Param("record") BaseRoles record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据主键更新属性不为空的记录
	 */
	int updateByPrimaryKeySelective(BaseRoles record);

	/**
	 * 根据主键更新记录
	 */
	int updateByPrimaryKey(BaseRoles record);
}