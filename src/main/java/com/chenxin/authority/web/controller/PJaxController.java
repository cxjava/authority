package com.chenxin.authority.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页面
 * 
 * @author Maty Chen
 * @date 2011-11-2 下午2:03:20
 */
@Controller
public class PJaxController {

	/**
	 * 主页
	 */
	@RequestMapping("/typography")
	public String home() {
		// 转到登录页面
		return "pjax/typography";
	}

	/**
	 * 主页面
	 */
	@RequestMapping("/code")
	public String main1() {
		return "pjax/code";
	}

	/**
	 * 主页面
	 */
	@RequestMapping("/temp")
	public String temp() {
		return "temp";
	}

	/**
	 * 头部
	 */
	@RequestMapping("/buttons")
	public String header() {
		return "pjax/buttons";
	}

}
