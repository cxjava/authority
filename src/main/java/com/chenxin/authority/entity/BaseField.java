package com.chenxin.authority.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统字段设置表
 */
@Entity
@Table(name = "t_base_field")
@Data
public class BaseField extends IdEntity {

    /**
     * 字段
     */
    private String field;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段值
     */
    private String valueField;

    /**
     * 字段显示值
     */
    private String displayField;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 排序
     */
    private Integer sort;

}