package com.chenxin.authority.service;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Services基本测试类<br>
 * 其他类继承即可
 * 
 * @author Maty Chen
 * @date 2011-08-10 下午04:34:17
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("H2")
public class Services{

}
