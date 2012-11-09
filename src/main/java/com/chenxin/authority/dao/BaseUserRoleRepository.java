package com.chenxin.authority.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseUserRole;

public interface BaseUserRoleRepository extends PagingAndSortingRepository<BaseUserRole, Long>, JpaSpecificationExecutor<BaseUserRole> {

}
