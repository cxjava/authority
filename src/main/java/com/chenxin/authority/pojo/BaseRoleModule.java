package com.chenxin.authority.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * role info and module info table
 */
@Entity
@Table(name = "t_base_role_module")
public class BaseRoleModule extends IdEntity {

	private Long roleId;
	private Long moduleId;

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the moduleId
	 */
	public Long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId
	 *            the moduleId to set
	 */
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

}