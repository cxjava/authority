package com.chenxin.authority.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseRoleModule;

public interface BaseRoleModuleRepository extends PagingAndSortingRepository<BaseRoleModule, Long>, JpaSpecificationExecutor<BaseRoleModule> {

	@Modifying
	@Query("delete BaseRoleModule u where u.roleId = ?1")
	Integer deleteByRoleId(Long roleId);
	
	List<BaseRoleModule> findByRoleId(Long roleId);
}
