package com.zteng.moraleducation.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.service.ISysRoleService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@Controller
@RequestMapping("/role")
public class SysRoleController {
    @Autowired
    private ISysRoleService roleService;

    @ApiOperation("查询角色")
    @GetMapping("/list")
    public CommonResult<IPage<SysRole>> list(@RequestParam(required = false) String name,
                                            @RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "10")Integer pageSize){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(StringUtils.isNoneBlank(name),"name", name);
        return CommonResult.success(roleService.page(new Page<>(pageNo, pageSize), wrapper));
    }


    @ApiOperation("新增角色")
    @PostMapping("/add")
    public CommonResult create(@Validated(SysRole.Save.class) @RequestBody SysRole sysRole){
        return CommonResult.success(roleService.save(sysRole));
    }


    @ApiOperation("修改角色")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated(SysRole.Update.class) @RequestBody SysRole sysRole){
        return CommonResult.success(roleService.updateById(sysRole));
    }


    @ApiOperation("删除角色")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam Long id){
        return CommonResult.success(roleService.removeById(id));
    }
}
