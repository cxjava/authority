package com.chenxin.authority.dao;

import com.chenxin.authority.entity.BaseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaseModuleRepository extends JpaRepository<BaseModule, Long>, JpaSpecificationExecutor<BaseModule>,
        BaseModuleRepositoryDao {

    @Modifying
    @Query("delete BaseModule u where u.parentId = ?1")
    Integer deleteByParentUrl(Long parentId);

    @Query("SELECT distinct a FROM BaseModule a, BaseRoleModule b, BaseRole c, "
            + "BaseUserRole d, BaseUser e WHERE a.id = b.moduleId AND b.roleId = c.id "
            + "AND c.id = d.roleId AND d.userId = e.id and e.id=?1 order by a.id asc")
    List<BaseModule> findByUserId(Long userId);

    List<BaseModule> findByLeaf(Integer leaf);

}
