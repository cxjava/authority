package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseRoleModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaseRoleModuleRepository extends JpaRepository<BaseRoleModule, Long>, JpaSpecificationExecutor<BaseRoleModule> {

    @Modifying
    @Query("delete BaseRoleModule u where u.roleId = ?1")
    Integer deleteByRoleId(Long roleId);

    List<BaseRoleModule> findByRoleId(Long roleId);
}
