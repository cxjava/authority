package com.chenxin.authority.dao;

import java.util.List;
import java.util.Map;

import com.chenxin.authority.pojo.BaseModule;

public interface BaseModuleRepositoryDao {
	List<BaseModule> selectAllModules(Map<String, Object> parameters);
}
