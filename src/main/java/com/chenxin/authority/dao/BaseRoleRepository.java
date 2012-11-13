package com.chenxin.authority.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseRole;

public interface BaseRoleRepository extends PagingAndSortingRepository<BaseRole, Long>, JpaSpecificationExecutor<BaseRole> {

	Page<BaseRole> findByRoleName(String roleName,Pageable pageable);
}
