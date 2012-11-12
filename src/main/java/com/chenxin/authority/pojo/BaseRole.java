package com.chenxin.authority.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;

/**
 * role info table
 */
@Entity
@Table(name = "t_base_role")
public class BaseRole extends IdEntity {

	/** role name */
	@NotBlank
	private String roleName;
	/** role description */
	private String roleDesc;

//	private List<BaseModule> moduleList = new ArrayList<BaseModule>();

	/**
	 * @return the roleName role name
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set role name
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the roleDesc role description
	 */
	public String getRoleDesc() {
		return roleDesc;
	}

	/**
	 * @param roleDesc
	 *            the roleDesc to set role description
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

//	/**
//	 * @return the moduleList
//	 */
//	// 多对多定义
//	@ManyToMany
//	@JoinTable(name = "t_base_role_module", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "moduleId") })
//	// Fecth策略定义
//	@Fetch(FetchMode.SUBSELECT)
//	// 集合按id排序
//	@OrderBy("id ASC")
//	public List<BaseModule> getModuleList() {
//		return moduleList;
//	}
//
//	/**
//	 * @param moduleList
//	 *            the moduleList to set
//	 */
//	public void setModuleList(List<BaseModule> moduleList) {
//		this.moduleList = moduleList;
//	}

}