package com.chenxin.authority.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseUserRole;

public interface BaseUserRoleRepository extends PagingAndSortingRepository<BaseUserRole, Long>, JpaSpecificationExecutor<BaseUserRole> {
	
	@Query("select count(*) from BaseUserRole u where u.roleId = ?1")
	Long findByRoleId(Long roleId);
	@Modifying
	@Query("delete BaseUserRole u where u.userId = ?1")
	Integer deleteByUserId(Long userId);
	
	List<BaseUserRole> findByUserId(Long userId);
}
