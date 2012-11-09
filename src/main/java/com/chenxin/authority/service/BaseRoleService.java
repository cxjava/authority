package com.chenxin.authority.service;

import com.chenxin.authority.pojo.BaseRole;
import com.chenxin.authority.pojo.ExtPager;

import java.util.Map;

import org.springframework.data.domain.Page;

public interface BaseRoleService {

	Page<BaseRole> selectByParameters(ExtPager pager, Map<String, Object> parameters);

	/**
	 * 保存角色
	 * 
	 * @param role
	 * @return 00：失败，01：成功 ,其他情况
	 */
	BaseRole saveRole(BaseRole role);

	/**
	 * 根据主键删除
	 * 
	 * @param criteria
	 * @return 00：失败，01：成功 ,其他情况
	 */
	void deleteByPrimaryKey(Long roleId);
}