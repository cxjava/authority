package com.chenxin.authority.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * role info and user info table
 */
@Entity
@Table(name = "t_base_user_role")
public class BaseUserRole extends IdEntity {

	private Long roleId;
	private Long userId;

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
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}