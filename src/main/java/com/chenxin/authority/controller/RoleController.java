package com.chenxin.authority.controller;

import com.chenxin.authority.entity.*;
import com.chenxin.authority.service.BaseRoleService;
import com.chenxin.authority.util.WebConstants;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 角色
 *
 * @author Maty Chen
 * @date 2011-10-21 下午2:49:31
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private BaseRoleService baseRolesService;


    /**
     * index
     */
    @RequestMapping(method = RequestMethod.GET)
    public String role() {
        return "user/role";
    }

    /**
     * 查找所有的角色
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object all(ExtPager pager, @RequestParam(required = false) String roleName) {
        Map<String, Object> parameters = Maps.newHashMap();
        if (StringUtils.isNotBlank(roleName)) {
            parameters.put("LIKE_roleName", roleName);
        }
        Page<BaseRole> page = this.baseRolesService.selectByParameters(pager, parameters);
        return new ExtGridReturn(page.getTotalElements(), page.getContent());
    }

    /**
     * 保存角色信息
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object save(BaseRole role) {
        try {
            if (role == null) {
                return new ExtReturn(false, "角色不能为空!");
            }
            if (StringUtils.isBlank(role.getRoleName())) {
                return new ExtReturn(false, "角色名称不能为空!");
            }
            this.baseRolesService.saveRole(role);
            return new ExtReturn(true, "保存成功！");
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }

    /**
     * 删除该角色
     */
    @RequestMapping("/del/{roleId}")
    @ResponseBody
    public Object delete(@PathVariable Long roleId) {
        try {
            if (null == roleId) {
                return new ExtReturn(false, "角色主键不能为空！");
            }
            this.baseRolesService.deleteByPrimaryKey(roleId);
            return new ExtReturn(true, "删除成功！");
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }
}
