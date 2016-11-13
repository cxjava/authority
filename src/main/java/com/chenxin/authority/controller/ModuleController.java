package com.chenxin.authority.controller;

import com.alibaba.fastjson.JSON;
import com.chenxin.authority.entity.*;
import com.chenxin.authority.service.BaseModuleService;
import com.chenxin.authority.util.WebConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 后台资源、系统菜单相关
 *
 * @author Maty Chen
 * @date 2011-10-31 下午10:16:24
 */
@Controller
@RequestMapping("/module")
public class ModuleController {

    private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);
    @Autowired
    private BaseModuleService baseModulesService;

    /**
     * index
     */
    @RequestMapping(method = RequestMethod.GET)
    public String module(Model model) {
        // 先查出所有的父节点
        Map<String, Object> map = this.baseModulesService.selectParentModule();
        map.put("0", "主菜单");
        model.addAttribute("moduleMap", JSON.toJSONString(map));
        return "user/module";
    }

    /**
     * 查找系统的所有菜单
     *
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public void all(HttpServletResponse response) throws IOException {
        Tree tree = this.baseModulesService.selectAllModules();
        String json = JSON.toJSONString(tree.getChildren());
        // 加入check
        response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
//        IOUtils.copy(IOUtils.toInputStream(json.replaceAll("\"leaf", "\"checked\":false,\"leaf"), Charset.defaultCharset()),response.getWriter(),Charset.defaultCharset());
        response.getWriter().print(json.replaceAll("\"leaf", "\"checked\":false,\"leaf"));
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 所有菜单信息
     */
    @RequestMapping("/all")
    @ResponseBody
    public Object all(ExtPager pager, @RequestParam(required = false) String moduleName) {
        try {
            Map<String, Object> parameters = Maps.newHashMap();
            if (StringUtils.isNotBlank(moduleName)) {
                parameters.put("LIKE_moduleName", moduleName);
            }
            Page<BaseModule> page = this.baseModulesService.selectByParameters(pager, parameters);
            return new ExtGridReturn(page.getTotalElements(), page.getContent());
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }

    /**
     * 加载此角色的所有资源
     */
    @RequestMapping("/{roleId}")
    @ResponseBody
    public Object selectModulesByRoleId(@PathVariable Long roleId) {
        try {
            if (null == roleId) {
                return new ExtReturn(false, "角色ID不能为空！");
            }
            return this.baseModulesService.selectModuleByRoleId(roleId);
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }

    /**
     * 保存角色的系统菜单信息
     */
    @RequestMapping("/saverole")
    @ResponseBody
    public Object save(@RequestParam Long roleId, @RequestParam String moduleIds) {
        try {
            ArrayList<Long> modulesIdList = Lists.newArrayList();
            if (null == roleId) {
                return new ExtReturn(false, "角色不能为空！");
            }
            if (StringUtils.isBlank(moduleIds)) {
                return new ExtReturn(false, "选择的资源不能为空！");
            } else {
                String[] modules = StringUtils.split(moduleIds, ",");
                if (null == modules || modules.length == 0) {
                    return new ExtReturn(false, "选择的资源不能为空！");
                }
                for (int i = 0; i < modules.length; i++) {
                    modulesIdList.add(new Long(modules[i]));
                }
            }
            logger.debug("save() - String roleId={}", roleId);
            logger.debug("save() - String moduleIds={}", moduleIds);
            String result = this.baseModulesService.saveModule(roleId, modulesIdList);
            if ("01".equals(result)) {
                return new ExtReturn(true, "保存成功！");
            } else if ("00".equals(result)) {
                return new ExtReturn(false, "保存失败！");
            } else {
                return new ExtReturn(false, result);
            }
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }

    /**
     * 保存系统菜单信息
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object save(BaseModule modules) {
        try {
            if (modules == null) {
                return new ExtReturn(false, "模块不能为空！");
            }
            if (StringUtils.isBlank(modules.getModuleName())) {
                return new ExtReturn(false, "模块名称不能为空！");
            }
            this.baseModulesService.saveModules(modules);
            return new ExtReturn(true, "保存成功！");
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }

    /**
     * 删除该模块
     */
    @RequestMapping("/del/{moduleId}")
    @ResponseBody
    public Object delete(@PathVariable Long moduleId) {
        try {
            if (null == moduleId) {
                return new ExtReturn(false, "模块主键不能为空！");
            }
            this.baseModulesService.delete(moduleId);
            return new ExtReturn(true, "删除成功！");
        } catch (Exception e) {
            logger.error(WebConstants.EXCEPTION, e);
            return new ExceptionReturn(e);
        }
    }
}
