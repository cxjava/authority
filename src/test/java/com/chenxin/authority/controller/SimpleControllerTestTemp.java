package com.chenxin.authority.controller;

import org.junit.Test;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * controller基本测试类
 *
 * @author Maty Chen
 * @date 2011-12-7 下午4:17:51
 */
public class SimpleControllerTestTemp extends Controller {

    @Test
    public void testSimple() throws Exception {
        RequestBuilder request = null;
        request = get("/findpwd");


        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}
