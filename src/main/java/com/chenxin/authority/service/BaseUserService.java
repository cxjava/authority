package com.chenxin.authority.service;

import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.ExtPager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface BaseUserService {

	Page<BaseUser> selectByParameters(ExtPager pager, Map<String, Object> parameters);

	
	List<BaseUserRole> selectRolesByUserId(Long userId);

	/**
	 * 找回用户的密码
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String updatePassword(Map<String, Object> parameters) ;
	/**
	 * 找回用户的密码
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String findPassword(BaseUser user) ;

	/**
	 * 重置用户的密码
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String resetPwdByPrimaryKey(Long userId);

	/**
	 * 根据主键删除
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	void deleteByPrimaryKey(Long userId);

	/**
	 * 用户登录查找
	 * 
	 * @param criteria
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String selectByAccount(Map<String, Object> parameters);

	/**
	 * 验证用户名是否注册
	 * 
	 * @param example
	 * @return 00：已经注册，01：未注册 ,其他情况
	 */
	boolean validateAccount(String account);

	/**
	 * 更新用户密码
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	void updateUserPassword(Map<String, Object> parameters);

	/**
	 * 保存用户
	 * 
	 * @param example
	 * @return 00：失败，01：成功 ,其他情况
	 */
	String saveUser(BaseUser user,Collection<Long> roleIds);

	/**
	 * 保存用户自己更新的信息
	 * 
	 * @param user
	 */
	void update(BaseUser user);
	
	BaseUser findOne(Long userId);
}