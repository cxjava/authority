package com.chenxin.authority.pojo;

import java.io.Serializable;

/**
 * 角色模块表
 */
public class BaseRoleModule implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色模块ID
	 */
	private String roleModuleId;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 模块ID
	 */
	private Integer moduleId;

	/**
	 * @return 角色模块ID
	 */
	public String getRoleModuleId() {
		return roleModuleId;
	}

	/**
	 * @param roleModuleId
	 *            角色模块ID
	 */
	public void setRoleModuleId(String roleModuleId) {
		this.roleModuleId = roleModuleId;
	}

	/**
	 * @return 角色ID
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 *            角色ID
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

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
}