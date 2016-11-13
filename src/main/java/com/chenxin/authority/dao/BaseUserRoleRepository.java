package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaseUserRoleRepository extends JpaRepository<BaseUserRole, Long>, JpaSpecificationExecutor<BaseUserRole> {

    @Query("select count(*) from BaseUserRole u where u.roleId = ?1")
    Long findByRoleId(Long roleId);

    @Modifying
    @Query("delete BaseUserRole u where u.userId = ?1")
    Integer deleteByUserId(Long userId);

    List<BaseUserRole> findByUserId(Long userId);
}
