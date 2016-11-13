package com.chenxin.authority.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 主页面
 *
 * @author Maty Chen
 * @date 2011-11-2 下午2:03:20
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        // 转到登录页面
        return "login";
    }

    /**
     * 主页面
     */
    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    /**
     * 头部
     */
    @RequestMapping("/header")
    public String header() {
        return "header";
    }

    /**
     * 欢迎界面
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
}
