package com.chenxin.authority.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.chenxin.authority.pojo.BaseModule;

public class BaseModuleRepositoryTest extends Dao {
	// private static final Logger logger =
	// LoggerFactory.getLogger(BaseModuleRepositoryTest.class);
	@Autowired
	private BaseModuleRepository repository;

	private BaseModule object;
	private List<BaseModule> list = null;

	@Before
	public void before() {
		object = new BaseModule();
		object.setModuleName("moduleName");
		object.setModuleUrl("moduleUrl");
		object.setLeaf(1);
		object.setParentId(1L);
	}

	@Test
	public void testSelectAllModules() {
		list = this.repository.selectAllModules(null);
		assertNotNull(list);
		assertNotNull(list.size());
		assertTrue(list.size() > 1);
	}

	@Test
	public void testFindByUserId() {
		list = this.repository.findByUserId(1L);
		assertNotNull(list);
		assertNotNull(list.size());
		assertTrue(list.size() > 0);
	}

	@Test
	public void testDeleteByParentUrl() {
		object = this.repository.save(object);
		int result = this.repository.deleteByParentUrl(1L);
		assertTrue(result >= 1);
	}

	@Test
	public void testFindByLeaf() {
		object = this.repository.save(object);
		list = this.repository.findByLeaf(1);
		assertNotNull(list);
		assertNotNull(list.size());
		assertTrue(list.size() >= 1);
	}
}
