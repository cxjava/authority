package com.chenxin.authority.service.impl;

import com.chenxin.authority.dao.BaseRoleModuleRepository;
import com.chenxin.authority.dao.BaseRoleRepository;
import com.chenxin.authority.dao.BaseUserRoleRepository;
import com.chenxin.authority.pojo.BaseRole;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.service.BaseRoleService;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BaseRoleServiceImpl implements BaseRoleService {
	@Autowired
	private BaseRoleRepository baseRolesRepository;
	@Autowired
	private BaseUserRoleRepository baseUserRoleRepository;
	@Autowired
	private BaseRoleModuleRepository baseRoleModuleRepository;

	private static final Logger logger = LoggerFactory.getLogger(BaseRoleServiceImpl.class);

	@Override
	public int countByExample(Criteria example) {
		int count = this.baseRolesRepository.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseRole selectByPrimaryKey(String roleId) {
		return this.baseRolesRepository.selectByPrimaryKey(roleId);
	}

	@Override
	public List<BaseRole> selectByExample(Criteria example) {
		return this.baseRolesRepository.selectByExample(example);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String saveRole(BaseRole role) {
		int result = 0;
		if (StringUtils.isBlank(role.getId())) {
			result = this.baseRolesRepository.insertSelective(role);
		} else {
			result = this.baseRolesRepository.updateByPrimaryKeySelective(role);
		}
		return result > 0 ? "01" : "00";
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String deleteByPrimaryKey(Criteria criteria) {
		String roleId = criteria.getString("roleId");
		int result = 0;
		// TODO 其他用户拥有该角色，还不能删除
		int count = this.baseUserRoleRepository.countByExample(criteria);
		if (count > 0) {
			return "其他用户拥有该角色，还不能删除";
		}
		this.baseRoleModuleRepository.deleteByExample(criteria);
		result = this.baseRolesRepository.deleteByPrimaryKey(roleId);
		return result > 0 ? "01" : "00";
	}
}