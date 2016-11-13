package com.chenxin.authority.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统字段设置表
 */
@Entity
@Table(name = "t_base_field")
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

    /**
     * @return 字段
     */
    public String getField() {
        return field;
    }

    /**
     * @param field 字段
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return 字段名称
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName 字段名称
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return 字段值
     */
    public String getValueField() {
        return valueField;
    }

    /**
     * @param valueField 字段值
     */
    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    /**
     * @return 字段显示值
     */
    public String getDisplayField() {
        return displayField;
    }

    /**
     * @param displayField 字段显示值
     */
    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    /**
     * @return 是否启用
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * @param enabled 是否启用
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}