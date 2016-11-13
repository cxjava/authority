package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseModule;

import java.util.List;
import java.util.Map;

public interface BaseModuleRepositoryDao {
    List<BaseModule> selectAllModules(Map<String, Object> parameters);
}
