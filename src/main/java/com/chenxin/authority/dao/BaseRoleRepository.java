package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseRoleRepository extends JpaRepository<BaseRole, Long>, JpaSpecificationExecutor<BaseRole> {

}
