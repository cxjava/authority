package com.chenxin.authority.dao;

import com.chenxin.authority.pojo.BaseUsers;
import com.chenxin.authority.pojo.Criteria;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseUsersMapper {
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
	int deleteByPrimaryKey(String userId);

	/**
	 * 保存记录,不管记录里面的属性是否为空
	 */
	int insert(BaseUsers record);

	/**
	 * 保存属性不为空的记录
	 */
	int insertSelective(BaseUsers record);

	/**
	 * 根据条件查询记录集
	 */
	List<BaseUsers> selectByExample(Criteria example);

	/**
	 * 根据主键查询记录
	 */
	BaseUsers selectByPrimaryKey(String userId);

	/**
	 * 根据条件更新属性不为空的记录
	 */
	int updateByExampleSelective(@Param("record") BaseUsers record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据条件更新记录
	 */
	int updateByExample(@Param("record") BaseUsers record, @Param("condition") Map<String, Object> condition);

	/**
	 * 根据主键更新属性不为空的记录
	 */
	int updateByPrimaryKeySelective(BaseUsers record);

	/**
	 * 根据主键更新记录
	 */
	int updateByPrimaryKey(BaseUsers record);
}