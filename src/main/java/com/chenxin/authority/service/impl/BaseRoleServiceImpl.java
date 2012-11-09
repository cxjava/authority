package com.chenxin.authority.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenxin.authority.common.utils.JpaTools;
import com.chenxin.authority.dao.BaseRoleModuleRepository;
import com.chenxin.authority.dao.BaseRoleRepository;
import com.chenxin.authority.dao.BaseUserRoleRepository;
import com.chenxin.authority.pojo.BaseRole;
import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.service.BaseRoleService;
import com.chenxin.authority.service.ServiceException;
import com.google.common.collect.Maps;

@Service
@Transactional
public class BaseRoleServiceImpl implements BaseRoleService {
	@Autowired
	private BaseRoleRepository baseRolesRepository;
	@Autowired
	private BaseUserRoleRepository baseUserRoleRepository;
	@Autowired
	private BaseRoleModuleRepository baseRoleModuleRepository;

	private static final Logger logger = LoggerFactory.getLogger(BaseRoleServiceImpl.class);

	@Override
	public BaseRole saveRole(BaseRole role) {
		return this.baseRolesRepository.save(role);
	}

	@Override
	public void deleteByPrimaryKey(Long roleId) {
		Long count = this.baseUserRoleRepository.findByRoleId(roleId);
		if (count > 0) {
			logger.error("其他用户拥有该角色，还不能删除!");
			throw new ServiceException("其他用户拥有该角色，还不能删除!", "");
		}
		this.baseRoleModuleRepository.deleteByRoleId(roleId);
		this.baseRolesRepository.delete(roleId);
	}

	@Override
	public Page<BaseRole> selectByParameters(ExtPager pager, Map<String, Object> parameters) {
		PageRequest pageable = JpaTools.getPageRequest(pager, "");
		Specification<BaseRole> spec = JpaTools.getSpecification(parameters, BaseRole.class);
		return this.baseRolesRepository.findAll(spec, pageable);
	}
}