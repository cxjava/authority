package com.chenxin.authority.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * role info and user info table
 */
@Entity
@Table(name = "t_base_user_role")
@Data
public class BaseUserRole extends IdEntity {

    private Long roleId;
    private Long userId;

}