package com.chenxin.authority.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseModule;

public interface BaseModuleRepository extends PagingAndSortingRepository<BaseModule, Long>, JpaSpecificationExecutor<BaseModule> {

}
