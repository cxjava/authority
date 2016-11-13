package com.chenxin.authority.service;

import com.chenxin.authority.entity.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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

	private BaseModule getBaseModule() {
		base = new BaseModule();
		base.setModuleName("aaa");
		base.setModuleUrl("url");
		base.setExpanded(1);
		return base;
	}

	@Test
	public void TestSelectAllModules() {
		Tree tree = service.selectAllModules();
		assertNotNull(tree);
	}

	@Test
	public void TestSelectModulesByUser() {
		BaseUser user = new BaseUser();
		user.setId(1L);
		Tree tree = service.selectModulesByUser(user);
		assertNotNull(tree);
	}

	@Test
	public void TestSaveModule() {
		List<Long> modulesIdList = Lists.newArrayList();
		for (Long j = 1L; j < 5; j++) {
			modulesIdList.add(j);
		}
		String result = service.saveModule(1L, modulesIdList);
		assertEquals(result, "01");
	}

	@Test
	public void TestSaveModules() {
		service.saveModules(base);
		assertNotNull(base.getId());
	}

	@Test
	public void TestDelete() {
		service.delete(1L);
	}

	@Test
	public void TestSelectByParameters() {
		this.service.saveModules(getBaseModule());
		this.service.saveModules(getBaseModule());
		this.service.saveModules(getBaseModule());

		ExtPager pager = new ExtPager();
		Map<String, Object> parameters = Maps.newHashMap();

		pager.setLimit(10);
		pager.setStart(0);
		pager.setDir("desc");
		pager.setSort("moduleName");

		Page<BaseModule> pages = this.service.selectByParameters(pager, parameters);
		assertNotNull(pages);
		assertTrue(pages.getTotalElements() >= 3);
		assertNotNull(pages.getContent());
		assertTrue(pages.getContent().size() >= 3);
	}

	@Test
	public void TestSelectModuleByRoleId() {
		List<BaseRoleModule> list = service.selectModuleByRoleId(1L);
		assertNotNull(list);
	}

	@Test
	public void TestSelectParentModule() {
		Map<String, Object> map = service.selectParentModule();
		assertNotNull(map);
	}

}
