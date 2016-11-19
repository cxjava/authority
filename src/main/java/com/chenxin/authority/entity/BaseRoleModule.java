package com.chenxin.authority.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * role info and module info table
 */
@Entity
@Table(name = "t_base_role_module")
@Data
public class BaseRoleModule extends IdEntity {

    private Long roleId;
    private Long moduleId;

}