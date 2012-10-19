package com.chenxin.authority.pojo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 系统模块表
 */
public class BaseModules implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 模块ID
	 */
	private Integer moduleId;

	/**
	 * 模块名称
	 */
	private String moduleName;

	/**
	 * 模块URL
	 */
	private String moduleUrl;

	/**
	 * 父模块ID
	 */
	private Integer parentId;

	/**
	 * 叶子节点(0:树枝节点;1:叶子节点)
	 */
	private Short leaf;

	/**
	 * 展开状态(1:展开;0:收缩)
	 */
	private Short expanded;

	/**
	 * 显示顺序
	 */
	private Short displayIndex;

	/**
	 * 是否显示 0:否 1:是
	 */
	private Short isDisplay;

	/**
	 * 模块英文名称
	 */
	private String enModuleName;

	/**
	 * 图标或者样式
	 */
	private String iconCss;

	/**
	 * 节点说明
	 */
	private String information;

	/**
	 * @return 模块ID
	 */
	public Integer getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId
	 *            模块ID
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return 模块名称
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            模块名称
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return 模块URL
	 */
	public String getModuleUrl() {
		return moduleUrl;
	}

	/**
	 * @param moduleUrl
	 *            模块URL
	 */
	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	/**
	 * @return 父模块ID
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            父模块ID
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return 叶子节点(0:树枝节点;1:叶子节点)
	 */
	public Short getLeaf() {
		return leaf;
	}

	/**
	 * @param leaf
	 *            叶子节点(0:树枝节点;1:叶子节点)
	 */
	public void setLeaf(Short leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return 展开状态(1:展开;0:收缩)
	 */
	public Short getExpanded() {
		return expanded;
	}

	/**
	 * @param expanded
	 *            展开状态(1:展开;0:收缩)
	 */
	public void setExpanded(Short expanded) {
		this.expanded = expanded;
	}

	/**
	 * @return 显示顺序
	 */
	public Short getDisplayIndex() {
		return displayIndex;
	}

	/**
	 * @param displayIndex
	 *            显示顺序
	 */
	public void setDisplayIndex(Short displayIndex) {
		this.displayIndex = displayIndex;
	}

	/**
	 * @return 是否显示 0:否 1:是
	 */
	public Short getIsDisplay() {
		return isDisplay;
	}

	/**
	 * @param isDisplay
	 *            是否显示 0:否 1:是
	 */
	public void setIsDisplay(Short isDisplay) {
		this.isDisplay = isDisplay;
	}

	/**
	 * @return 模块英文名称
	 */
	@JsonIgnore
	public String getEnModuleName() {
		return enModuleName;
	}

	/**
	 * @param enModuleName
	 *            模块英文名称
	 */
	public void setEnModuleName(String enModuleName) {
		this.enModuleName = enModuleName;
	}

	/**
	 * @return 图标或者样式
	 */
	public String getIconCss() {
		return iconCss;
	}

	/**
	 * @param iconCss
	 *            图标或者样式
	 */
	public void setIconCss(String iconCss) {
		this.iconCss = iconCss;
	}

	/**
	 * @return 节点说明
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information
	 *            节点说明
	 */
	public void setInformation(String information) {
		this.information = information;
	}
}