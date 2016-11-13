package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long>, JpaSpecificationExecutor<BaseUser> {

    List<BaseUser> findByAccount(String account);

    List<BaseUser> findByAccountAndEmail(String account, String email);

}
