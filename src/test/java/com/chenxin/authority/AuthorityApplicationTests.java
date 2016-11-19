package com.chenxin.authority;

import com.chenxin.authority.entity.BaseModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AuthorityApplicationTests {

	@Test
	public void contextLoads() {
		BaseModule baseModule = new BaseModule();
		baseModule.setId(123L);
		baseModule.setDisplayIndex(1);
		baseModule.setEnModuleName("dddd");
		log.info("{}",baseModule);
	}

}
