package com.chenxin.authority.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * module info table
 */
@Entity
@Table(name = "t_base_module")
public class BaseModule extends IdEntity {

	/** module name */
	@NotBlank
	private String moduleName;
	/** role's description */
	private String moduleUrl;
	private Long parentUrl;
	private Integer leaf;
	private Integer expanded;
	private Integer displayIndex;
	private Integer isDisplay;
	private String enModuleName;
	private String iconCss;
	private String information;

	/**
	 * @return the moduleName module name
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set module name
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the moduleUrl role's description
	 */
	public String getModuleUrl() {
		return moduleUrl;
	}

	/**
	 * @param moduleUrl
	 *            the moduleUrl to set role's description
	 */
	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	/**
	 * @return the parentUrl
	 */
	public Long getParentUrl() {
		return parentUrl;
	}

	/**
	 * @param parentUrl
	 *            the parentUrl to set
	 */
	public void setParentUrl(Long parentUrl) {
		this.parentUrl = parentUrl;
	}

	/**
	 * @return the leaf
	 */
	public Integer getLeaf() {
		return leaf;
	}

	/**
	 * @param leaf
	 *            the leaf to set
	 */
	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the expanded
	 */
	public Integer getExpanded() {
		return expanded;
	}

	/**
	 * @param expanded
	 *            the expanded to set
	 */
	public void setExpanded(Integer expanded) {
		this.expanded = expanded;
	}

	/**
	 * @return the displayIndex
	 */
	public Integer getDisplayIndex() {
		return displayIndex;
	}

	/**
	 * @param displayIndex
	 *            the displayIndex to set
	 */
	public void setDisplayIndex(Integer displayIndex) {
		this.displayIndex = displayIndex;
	}

	/**
	 * @return the isDisplay
	 */
	public Integer getIsDisplay() {
		return isDisplay;
	}

	/**
	 * @param isDisplay
	 *            the isDisplay to set
	 */
	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}

	/**
	 * @return the enModuleName
	 */
	public String getEnModuleName() {
		return enModuleName;
	}

	/**
	 * @param enModuleName
	 *            the enModuleName to set
	 */
	public void setEnModuleName(String enModuleName) {
		this.enModuleName = enModuleName;
	}

	/**
	 * @return the iconCss
	 */
	public String getIconCss() {
		return iconCss;
	}

	/**
	 * @param iconCss
	 *            the iconCss to set
	 */
	public void setIconCss(String iconCss) {
		this.iconCss = iconCss;
	}

	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information
	 *            the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

}