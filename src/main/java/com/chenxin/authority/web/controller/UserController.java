package com.chenxin.authority.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chenxin.authority.common.springmvc.DateConvertEditor;
import com.chenxin.authority.common.utils.EncryptUtil;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.service.BaseUserService;
import com.chenxin.authority.web.interseptor.WebConstants;
import com.google.common.collect.Maps;

/**
 * 用户相关
 * 
 * @author Maty Chen
 * @date 2011-10-20 上午11:45:06
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private BaseUserService baseUsersService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * index
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String user() {
		return "user/user";
	}

	/**
	 * 查找所有的用户
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object all(ExtPager pager, @RequestParam(required = false, defaultValue = "") String realName) {
		Map<String, Object> parameters = Maps.newHashMap();
		if (StringUtils.isNotBlank(realName)) {
			parameters.put("LIKE_realName", realName);
		}
		Page<BaseUser> page = this.baseUsersService.selectByParameters(pager, parameters);
		return new ExtGridReturn(page.getTotalElements(), page.getContent());
		
	}

	/**
	 * 用户修改密码页面
	 */
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	public String changePwd() {
		return "user/changepwd";
	}

	/**
	 * 修改自己的密码
	 */
	@RequestMapping(value = "/changepwd", method = RequestMethod.POST)
	@ResponseBody
	public Object changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String comparePassword,
			@RequestParam Long userId, HttpSession session) {
		try {
			if (null==userId) {
				return new ExtReturn(false, "用户ID不能为空！");
			}
			if (StringUtils.isBlank(oldPassword)) {
				return new ExtReturn(false, "原密码不能为空！");
			}
			if (StringUtils.isBlank(newPassword)) {
				return new ExtReturn(false, "新密码不能为空！");
			}
			if (StringUtils.isBlank(comparePassword)) {
				return new ExtReturn(false, "确认密码不能为空！");
			}
			if (!comparePassword.equals(newPassword)) {
				return new ExtReturn(false, "两次输入的密码不一致！");
			}
			BaseUser user = (BaseUser) session.getAttribute(WebConstants.CURRENT_USER);
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("userId", userId);
			// 传入的password已经md5过一次了,并且为小写
			parameters.put("oldPassword", oldPassword);
			// 传入的password已经md5过一次了,并且为小写
			parameters.put("newPassword", newPassword);
			
			// 比较原密码
			if (!userId.equals(user.getId()) || !EncryptUtil.match(oldPassword, user.getPassword())) {
				return new ExtReturn(false, "原密码不正确！请重新输入！");
			}
			
			this.baseUsersService.updateUserPassword(parameters);
			session.removeAttribute(WebConstants.CURRENT_USER);
			session.invalidate();
			return new ExtReturn(true, "修改密码成功！请重新登录！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 获取用户的所有角色
	 */
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Object myRole(@PathVariable Long userId) {
		try {
			logger.debug("{}", userId);
			List<BaseUserRole> list = this.baseUsersService.selectRolesByUserId(userId);
			return list;
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 重置用户的密码
	 */
	@RequestMapping("/reset/{userId}")
	@ResponseBody
	public Object resetPassword(@PathVariable Long userId) {
		try {
			if (null==userId) {
				return new ExtReturn(false, "用户主键不能为空！");
			}
			String result = this.baseUsersService.resetPwdByPrimaryKey(userId);
			if ("01".equals(result)) {
				return new ExtReturn(true, "重置密码成功！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "重置密码失败！");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 删除该用户
	 */
	@RequestMapping("/del/{userId}")
	@ResponseBody
	public Object delete(@PathVariable Long userId, HttpSession session) {
		try {
			if (null==userId) {
				return new ExtReturn(false, "用户主键不能为空！");
			}
			// 不能删除自己
			BaseUser user = (BaseUser) session.getAttribute(WebConstants.CURRENT_USER);
			if (userId.equals(user.getId())) {
				return new ExtReturn(false, "不能删除自己的帐号！");
			}
			this.baseUsersService.deleteByPrimaryKey(userId);
			return new ExtReturn(true, "删除成功！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 验证用户名是否注册
	 */
	@RequestMapping("/validate")
	@ResponseBody
	public Object validateAccount(@RequestParam(value = "account", required = false, defaultValue = "") String account,
			@RequestParam String userId) {
		try {
			if (StringUtils.isBlank(account)) {
				return new ExtReturn(false, "帐号不能为空!");
			}
			boolean result = this.baseUsersService.validateAccount(account);
			if (true==result) {
				return new ExtReturn(true, "帐号未被注册！");
			} else{
				return new ExtReturn(false, "帐号已经被注册！请重新填写!");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 保存用户信息
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(BaseUser user, @RequestParam Collection<Long> roleIds) {
		try {
			if (roleIds == null || roleIds.size() == 0) {
				return new ExtReturn(false, "请至少选择一个角色！");
			}
			if (StringUtils.isBlank(user.getAccount())) {
				return new ExtReturn(false, "帐号不能为空！");
			}
			String result = this.baseUsersService.saveUser( user, roleIds);
			if ("01".equals(result)) {
				return new ExtReturn(true, "保存成功！");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	/**
	 * 编辑用户
	 */
	@RequestMapping(value = "/myinfo", method = RequestMethod.GET)
	public String myinfo() {
		return "user/myinfo";
	}

	/**
	 * 保存用户自己编辑的信息
	 */
	@RequestMapping(value = "/myinfo", method = RequestMethod.POST)
	@ResponseBody
	public Object saveMyinfo(BaseUser user) {
		try {
			if (user == null) {
				return new ExtReturn(false, "用户不能为空！");
			}
			if (user.getId() == null) {
				return new ExtReturn(false, "用户ID不能为空！");
			}
			this.baseUsersService.update(user);
			return new ExtReturn(true, "用户信息更新成功！请重新登录！");
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

}
