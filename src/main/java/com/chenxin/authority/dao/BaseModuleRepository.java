package com.chenxin.authority.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseModule;

public interface BaseModuleRepository extends PagingAndSortingRepository<BaseModule, Long>, JpaSpecificationExecutor<BaseModule>,
		BaseModuleRepositoryDao {

	@Modifying
	@Query("delete BaseModule u where u.parentId = ?1")
	Integer deleteByParentUrl(Long parentId);
}
