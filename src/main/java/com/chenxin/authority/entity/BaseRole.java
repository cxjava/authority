package com.chenxin.authority.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * role info table
 */
@Entity
@Table(name = "t_base_role")
@Data
public class BaseRole extends IdEntity {

    /**
     * role name
     */
    @NotBlank
    private String roleName;
    /**
     * role description
     */
    private String roleDesc;

}