package com.chenxin.authority.service;

import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.Criteria;

import java.util.List;

public interface BaseUserRoleService {
	int countByExample(Criteria example);

	BaseUserRole selectByPrimaryKey(String userRoleId);

	List<BaseUserRole> selectByExample(Criteria example);

}