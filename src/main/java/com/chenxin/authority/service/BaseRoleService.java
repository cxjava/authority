package com.chenxin.authority.service;

import com.chenxin.authority.pojo.BaseRole;
import com.chenxin.authority.pojo.Criteria;
import java.util.List;

public interface BaseRoleService {
	int countByExample(Criteria example);

	BaseRole selectByPrimaryKey(String roleId);

	List<BaseRole> selectByExample(Criteria example);

	/**
	 * 保存角色
	 * 
	 * @param role
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String saveRole(BaseRole role);

	/**
	 * 根据主键删除
	 * 
	 * @param criteria
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String deleteByPrimaryKey(Criteria criteria);
}