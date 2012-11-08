package com.chenxin.authority.service.impl;

import com.chenxin.authority.dao.BaseRoleModuleRepository;
import com.chenxin.authority.pojo.BaseRoleModule;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.service.BaseRoleModuleService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseRoleModuleServiceImpl implements BaseRoleModuleService {
	@Autowired
	private BaseRoleModuleRepository repository;

	private static final Logger logger = LoggerFactory.getLogger(BaseRoleModuleServiceImpl.class);

	@Override
	public int countByExample(Criteria example) {
		int count = this.repository.countByExample(example);
		logger.debug("count: {}", count);
		return count;
	}

	@Override
	public BaseRoleModule selectByPrimaryKey(String roleModuleId) {
		return this.repository.selectByPrimaryKey(roleModuleId);
	}

	@Override
	public List<BaseRoleModule> selectByExample(Criteria example) {
		return this.repository.selectByExample(example);
	}
}