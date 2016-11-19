package com.chenxin.authority.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * module info table
 */
@Entity
@Table(name = "t_base_module")
@Data
@ToString(callSuper = true)
public class BaseModule extends IdEntity {

    /**
     * module name
     */
    @NotBlank
    private String moduleName;
    /**
     * role's description
     */
    private String moduleUrl;
    private Long parentId;
    private Integer leaf;
    private Integer expanded;
    private Integer displayIndex;
    private Integer isDisplay;
    private String enModuleName;
    private String iconCss;
    private String information;


}