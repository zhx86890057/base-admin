package com.zteng.moraleducation.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.entity.SysMenu;
import com.zteng.moraleducation.pojo.vo.MenuVO;
import com.zteng.moraleducation.service.ISysMenuService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@Controller
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;

    @ApiOperation("查询菜单")
    @GetMapping("/list")
    public CommonResult<List<MenuVO>> list(@RequestParam(required = false) String name){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(StringUtils.isNoneBlank(name), "name", name);
        List<SysMenu> menuList = menuService.list(wrapper);
        List<MenuVO> menuVOS = JSON.parseArray(JSON.toJSONString(menuList), MenuVO.class);
        return CommonResult.success(menuService.buildTree(menuVOS));
    }


    @ApiOperation("新增菜单")
    @PostMapping("/add")
    public CommonResult create(@Validated(SysDepartment.Save.class) @RequestBody SysMenu sysMenu){
        return CommonResult.success(menuService.save(sysMenu));
    }


    @ApiOperation("修改菜单")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('dept:edit')")
    public CommonResult<Boolean> update(@Validated(SysMenu.Update.class) @RequestBody SysMenu sysMenu){
        return CommonResult.success(menuService.updateById(sysMenu));
    }


    @ApiOperation("删除菜单")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam Long id){
        return CommonResult.success(menuService.removeById(id));
    }
}
