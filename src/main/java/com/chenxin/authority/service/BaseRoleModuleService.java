package com.chenxin.authority.service;

import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.Criteria;
import java.util.List;

public interface BaseRoleModuleService {
	int countByExample(Criteria example);

	BaseRoleModule selectByPrimaryKey(String roleModuleId);

	List<BaseRoleModule> selectByExample(Criteria example);
}