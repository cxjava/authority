package com.chenxin.authority.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chenxin.authority.common.springmvc.DateConvertEditor;
import com.chenxin.authority.pojo.BaseUserRole;
import com.chenxin.authority.pojo.BaseUser;
import com.chenxin.authority.pojo.Criteria;
import com.chenxin.authority.pojo.ExceptionReturn;
import com.chenxin.authority.pojo.ExtGridReturn;
import com.chenxin.authority.pojo.ExtPager;
import com.chenxin.authority.pojo.ExtReturn;
import com.chenxin.authority.service.BaseUserRoleService;
import com.chenxin.authority.service.BaseUserService;
import com.chenxin.authority.web.interseptor.WebConstants;

/**
 * 用户相关
 * 
 * @author chenxin
 * @date 2011-10-20 上午11:45:06
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private BaseUserService baseUsersService;
	@Autowired
	private BaseUserRoleService baseUserRoleService;

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
		Criteria criteria = new Criteria();
		// 设置分页信息
		if (pager.getLimit() != null && pager.getStart() != null) {
			criteria.setOracleEnd(pager.getLimit() + pager.getStart());
			criteria.setOracleStart(pager.getStart());
		}
		// 排序信息
		if (StringUtils.isNotBlank(pager.getDir()) && StringUtils.isNotBlank(pager.getSort())) {
			criteria.setOrderByClause(pager.getSort() + " " + pager.getDir());
		}
		if (StringUtils.isNotBlank(realName)) {
			criteria.put("realNameLike", realName);
		}
		List<BaseUser> list = this.baseUsersService.selectByExample(criteria);
		int total = this.baseUsersService.countByExample(criteria);
		return new ExtGridReturn(total, list);
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
			@RequestParam String userId, HttpSession session) {
		try {
			if (StringUtils.isBlank(userId)) {
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
			Criteria criteria = new Criteria();
			criteria.put("user", user);
			criteria.put("userId", userId);
			// 传入的password已经md5过一次了,并且为小写
			criteria.put("oldPassword", oldPassword);
			// 传入的password已经md5过一次了,并且为小写
			criteria.put("newPassword", newPassword);
			String result = this.baseUsersService.updateUserPassword(criteria);
			if ("01".equals(result)) {
				session.removeAttribute(WebConstants.CURRENT_USER);
				session.invalidate();
				return new ExtReturn(true, "修改密码成功！请重新登录！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "修改密码失败!");
			} else {
				return new ExtReturn(false, result);
			}
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
	public Object myRole(@PathVariable String userId) {
		try {
			Criteria criteria = new Criteria();
			criteria.put("userId", userId);
			logger.debug("{}", userId);
			List<BaseUserRole> list = this.baseUserRoleService.selectByExample(criteria);
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
	public Object resetPassword(@PathVariable String userId) {
		try {
			if (StringUtils.isBlank(userId)) {
				return new ExtReturn(false, "用户主键不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("userId", userId);
			String result = this.baseUsersService.resetPwdByPrimaryKey(criteria);
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
	public Object delete(@PathVariable String userId, HttpSession session) {
		try {
			if (StringUtils.isBlank(userId)) {
				return new ExtReturn(false, "用户主键不能为空！");
			}
			// 不能删除自己
			BaseUser user = (BaseUser) session.getAttribute(WebConstants.CURRENT_USER);
			if (userId.equals(user.getId())) {
				return new ExtReturn(false, "不能删除自己的帐号！");
			}
			Criteria criteria = new Criteria();
			criteria.put("userId", userId);
			String result = this.baseUsersService.deleteByPrimaryKey(criteria);
			if ("01".equals(result)) {
				return new ExtReturn(true, "删除成功！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "删除失败！");
			} else {
				return new ExtReturn(false, result);
			}
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
			Criteria criteria = new Criteria();
			if (StringUtils.isNotBlank(account)) {
				criteria.put("account", account);
			}
			if (StringUtils.isNotBlank(userId)) {
				criteria.put("userId", userId);
			}
			String result = this.baseUsersService.validateAccount(criteria);
			if ("01".equals(result)) {
				return new ExtReturn(true, "帐号未被注册！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "帐号已经被注册！请重新填写!");
			} else {
				return new ExtReturn(false, result);
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
	public Object save(BaseUser user, @RequestParam Collection<String> roleIds) {
		try {
			if (roleIds == null || roleIds.size() == 0) {
				return new ExtReturn(false, "请至少选择一个角色！");
			}
			if (StringUtils.isBlank(user.getAccount())) {
				return new ExtReturn(false, "帐号不能为空！");
			}
			Criteria criteria = new Criteria();
			criteria.put("roleIds", roleIds);
			criteria.put("user", user);
			String result = this.baseUsersService.saveUser(criteria);
			if ("01".equals(result)) {
				return new ExtReturn(true, "保存成功！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "保存失败！");
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
			if (user.getId()==null) {
				return new ExtReturn(false, "用户ID不能为空！");
			}
			String result = this.baseUsersService.updateByPrimaryKeySelective(user);
			if ("01".equals(result)) {
				return new ExtReturn(true, "用户信息更新成功！请重新登录！");
			} else if ("00".equals(result)) {
				return new ExtReturn(false, "用户信息更新失败！");
			} else {
				return new ExtReturn(false, result);
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return new ExceptionReturn(e);
		}
	}

	
}
