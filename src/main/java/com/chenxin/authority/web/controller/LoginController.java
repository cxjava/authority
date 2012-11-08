package com.chenxin.authority.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chenxin.authority.common.springmvc.DateConvertEditor;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.pojo.Tree;
import com.chenxin.authority.service.BaseModuleService;
import com.chenxin.authority.service.BaseUserService;
import com.chenxin.authority.web.interseptor.WebConstants;
import com.google.code.kaptcha.Constants;

/**
 * 用户登录相关
 * 
 * @author chenxin
 * @date 2011-10-20 上午11:45:00
 */
@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private BaseUserService baseUsersService;
	@Autowired
	private BaseModuleService baseModulesService;
	/** 限制时间 */
	@Value("${limit.millis:3600000}")
	private Long millis;

	@Autowired
	private MessageSource messageSource;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * 退出登录
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public Object logout(HttpSession session, Locale locale) {
		try {
			session.removeAttribute(WebConstants.CURRENT_USER);
			session.invalidate();
			return new ExtReturn(true, "退出系统成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 用户菜单treeMenu的数据
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/treeMenu")
	@ResponseBody
	public Object treeMenu(HttpSession session, HttpServletResponse response) {
		try {
			BaseUser user = (BaseUser) session.getAttribute(WebConstants.CURRENT_USER);
			// 得到的是根菜单
			Tree tree = this.baseModulesService.selectModulesByUser(user);
			// 返回根菜单下面的子菜单
			return tree.getChildren();
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 用户登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Object login(@RequestParam String account, @RequestParam String password, HttpSession session, HttpServletRequest request) {
		try {
			if (StringUtils.isBlank(account)) {
				return new ExtReturn(false, "帐号不能为空！");
			}
			if (StringUtils.isBlank(password)) {
				return new ExtReturn(false, "密码不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("account", account);
			criteria.put("passwordIn", password);
			criteria.put("loginip", this.getIpAddr(request));
			String result = this.baseUsersService.selectByBaseUser(criteria);
			if ("01".equals(result)) {
				BaseUser baseUser = (BaseUser) criteria.get("baseUser");
				session.setAttribute(WebConstants.CURRENT_USER, baseUser);
				logger.info("{}登陆成功", baseUser.getRealName());
				return new ExtReturn(true, "success");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "用户名或者密码错误!");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 取得客户端真实ip
	 * 
	 * @param request
	 * @return 客户端真实ip
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		logger.debug("1- X-Forwarded-For ip={}", ip);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.debug("2- Proxy-Client-IP ip={}", ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			logger.debug("3- WL-Proxy-Client-IP ip={}", ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			logger.debug("4- HTTP_CLIENT_IP ip={}", ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.debug("5- HTTP_X_FORWARDED_FOR ip={}", ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			logger.debug("6- getRemoteAddr ip={}", ip);
		}
		logger.info("finally ip={}", ip);
		return ip;
	}

	/**
	 * 转到找回用户密码页面
	 */
	@RequestMapping(value = "/findpwd", method = RequestMethod.GET)
	public String findpwd() {
		return "user/findpwd";
	}

	/**
	 * 找回用户密码
	 */
	@RequestMapping(value = "/findpwd", method = RequestMethod.POST)
	@ResponseBody
	public Object saveFindpwd(BaseUser user,@RequestParam String captcha,HttpSession session) {
		try {
			if (StringUtils.isBlank(captcha)) {
				return new ExtReturn(false, "验证码不能为空！");
			}
			Object sessionCaptcha =  session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if(null==sessionCaptcha){
				return new ExtReturn(false, "验证码已经失效!请重新输入新的验证码！");
			}
			if (!captcha.equalsIgnoreCase((String)sessionCaptcha)) {
				return new ExtReturn(false, "验证码输入不正确,请重新输入！");
			}
			//移除验证码，不能用同一个验证码重复提交来试探密码等，不要想铁道部的网站那样
			session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
			if (user == null) {
				return new ExtReturn(false, "用户不能为空！");
			}
			if (StringUtils.isBlank(user.getAccount())) {
				return new ExtReturn(false, "帐号不能为空！");
			}
			if (StringUtils.isBlank(user.getEmail())) {
				return new ExtReturn(false, "注册邮箱不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("user", user);
			String result = this.baseUsersService.findPassword(criteria);
			if ("01".equals(result)) {
				return new ExtReturn(true, "邮件发送成功！请登录注册邮箱查收！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "邮件发送失败！");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 找回用户密码时的重新设置密码的页面
	 */
	@RequestMapping(value = "/resetpwd/{token}/{userId}", method = RequestMethod.GET)
	public String resetpwd(@PathVariable String token, @PathVariable String userId, Model model) {
		BaseUser user = this.baseUsersService.selectByPrimaryKey(userId);
		if (user == null || !user.getPassword().equals(token.toLowerCase()) || compareTo(user.getLastLoginTime())) {
			model.addAttribute("error", "链接已经失效！");
			return "user/resetpwd";
		}
		model.addAttribute("t", token);
		model.addAttribute("u", userId);
		return "user/resetpwd";
	}

	/**
	 * 找回用户密码时的重新设置密码的页面
	 */
	@RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
	public String resetpwdSave(@RequestParam String u, @RequestParam String t, @RequestParam String newpwd, @RequestParam String renewpwd,
			Model model) {
		try {
			model.addAttribute("t", t);
			model.addAttribute("u", u);
			if (StringUtils.isBlank(u)) {
				model.addAttribute("msg", "密码修改失败！");
				return "user/resetpwd";
			}
			if (StringUtils.isBlank(t)) {
				model.addAttribute("msg", "密码修改失败！");
				return "user/resetpwd";
			}
			if (StringUtils.isBlank(newpwd)) {
				model.addAttribute("msg", "新密码不能为空！");
				return "user/resetpwd";
			}
			if (StringUtils.isBlank(renewpwd)) {
				model.addAttribute("msg", "确认密码不能为空！");
				return "user/resetpwd";
			}
			if (!renewpwd.equals(newpwd)) {
				model.addAttribute("msg", "新密码和确认密码输入不一致！");
				return "user/resetpwd";
			}
			Criteria criteria = new Criteria();
			criteria.put("token", t);
			criteria.put("userId", u);
			criteria.put("password", newpwd);
			String result = this.baseUsersService.updatePassword(criteria);
			if ("01".equals(result)) {
				model.addAttribute("msg", "密码修改成功！请重新登录");
			} else if ("00".equals(result)) {
				model.addAttribute("msg", "密码修改失败！");
			} else {
				model.addAttribute("msg", result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			model.addAttribute("msg", e.getMessage());
		}
		return "user/resetpwd";
	}

	/**
	 * 日期比较
	 * 
	 * @param date
	 * @return
	 */
	private boolean compareTo(Date date) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.setTime(date);
		long lastly = c.getTimeInMillis();
		// 60分钟1000*60*60;
		return (now - lastly) >= millis;
	}
}
