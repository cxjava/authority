package com.chenxin.authority.service.impl;

import com.chenxin.authority.dao.BaseRoleModuleMapper;
import com.chenxin.authority.dao.BaseRolesMapper;
import com.chenxin.authority.dao.BaseUserRoleMapper;
import com.chenxin.authority.pojo.BaseRoles;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.service.BaseRolesService;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BaseRolesServiceImpl implements BaseRolesService {
	@Autowired
	private BaseRolesMapper baseRolesMapper;
	@Autowired
	private BaseUserRoleMapper baseUserRoleMapper;
	@Autowired
	private BaseRoleModuleMapper baseRoleModuleMapper;

	private static final Logger logger = LoggerFactory.getLogger(BaseRolesServiceImpl.class);

	@Override
	public int countByExample(Criteria example) {
		int count = this.baseRolesMapper.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseRoles selectByPrimaryKey(String roleId) {
		return this.baseRolesMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public List<BaseRoles> selectByExample(Criteria example) {
		return this.baseRolesMapper.selectByExample(example);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveRole(BaseRoles role) {
		int result = 0;
		if (StringUtils.isBlank(role.getRoleId())) {
			result = this.baseRolesMapper.insertSelective(role);
		} else {
			result = this.baseRolesMapper.updateByPrimaryKeySelective(role);
		}
		return result > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String deleteByPrimaryKey(Criteria criteria) {
		String roleId = criteria.getString("roleId");
		int result = 0;
		// TODO 其他用户拥有该角色，还不能删除
		int count = this.baseUserRoleMapper.countByExample(criteria);
		if (count > 0) {
			return "其他用户拥有该角色，还不能删除";
		}
		this.baseRoleModuleMapper.deleteByExample(criteria);
		result = this.baseRolesMapper.deleteByPrimaryKey(roleId);
		return result > 0 ? "01" : "00";
	}
}