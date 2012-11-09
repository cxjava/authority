package com.chenxin.authority.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseRoleModule;

public interface BaseRoleModuleRepository extends PagingAndSortingRepository<BaseRoleModule, Long>, JpaSpecificationExecutor<BaseRoleModule> {

}
