package com.chenxin.authority.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chenxin.authority.pojo.BaseUser;

public interface BaseUserRepository extends PagingAndSortingRepository<BaseUser, Long>, JpaSpecificationExecutor<BaseUser> {

	List<BaseUser> findByAccount(String account);
	
	List<BaseUser> findByAccountAndEmail(String account,String email);

}
