package com.zteng.moraleducation.controller;


import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.pojo.entity.SysUser;
import com.zteng.moraleducation.pojo.param.UserParam;
import com.zteng.moraleducation.pojo.vo.DepartVO;
import com.zteng.moraleducation.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@Controller
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private ISysUserService userService;

    @ApiOperation("查询用户列表")
    @GetMapping("/list")
    public CommonResult<List<DepartVO>> getDepts(Principal user, @RequestBody UserParam userParam){
        //todo
        return CommonResult.success(null);
    }


    @ApiOperation("新增用户")
    @PostMapping("/add")
    public CommonResult create(@Validated(SysUser.Save.class) @RequestBody SysUser sysUser){
        return CommonResult.success(userService.save(sysUser));
    }


    @ApiOperation("修改用户")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated(SysUser.Update.class) @RequestBody SysUser sysUser){
        return CommonResult.success(userService.updateById(sysUser));
    }


    @ApiOperation("删除用户")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam Long id){
        return CommonResult.success(userService.removeById(id));
    }
}
