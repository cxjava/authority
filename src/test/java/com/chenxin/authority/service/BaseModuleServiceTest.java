package com.chenxin.authority.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenxin.authority.pojo.BaseModule;
import com.chenxin.authority.pojo.Tree;
import com.chenxin.authority.service.BaseModuleService;

/**
 * 
 * 
 * @author Maty Chen
 * @date 2011-12-7 下午2:27:04
 */
public class BaseModuleServiceTest extends Services {

	@Autowired
	private BaseModuleService service;

	private BaseModule base;

	@Before
	public void before() {
		base = new BaseModule();
		base.setModuleName("aaa");
		base.setModuleUrl("url");
		base.setExpanded(1);
	}

	@Test
	public void selectAllModules() {
		Tree tree = service.selectAllModules();
		assertNotNull(tree);
	}
}
