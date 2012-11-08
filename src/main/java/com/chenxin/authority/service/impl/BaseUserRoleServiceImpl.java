package com.chenxin.authority.service.impl;

import com.chenxin.authority.dao.BaseUserRoleRepository;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.service.BaseUserRoleService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseUserRoleServiceImpl implements BaseUserRoleService {
	@Autowired
	private BaseUserRoleRepository baseUserRoleRepository;

	private static final Logger logger = LoggerFactory.getLogger(BaseUserRoleServiceImpl.class);

	@Override
	public int countByExample(Criteria example) {
		int count = this.baseUserRoleRepository.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseUserRole selectByPrimaryKey(String userRoleId) {
		return this.baseUserRoleRepository.selectByPrimaryKey(userRoleId);
	}

	@Override
	public List<BaseUserRole> selectByExample(Criteria example) {
		return this.baseUserRoleRepository.selectByExample(example);
	}

}