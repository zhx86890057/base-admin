package com.zteng.moraleducation.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.constant.Constant;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.vo.DepartVO;
import com.zteng.moraleducation.service.ISysDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/department")
@Api(tags = "SysDepartmentController", description = "每周值班管理")
public class SysDepartmentController {
    @Autowired
    private ISysDepartmentService departmentService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("查询部门")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('dept:list')")
    public CommonResult<List<DepartVO>> getDepts(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Integer status){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(StringUtils.isNoneBlank(name), "name", name);
        List<SysDepartment> departmentList = departmentService.list(wrapper);
        List<DepartVO> departVOS = JSON.parseArray(JSON.toJSONString(departmentList), DepartVO.class);
        return CommonResult.success(departmentService.buildTree(departVOS));
    }


    @ApiOperation("新增部门")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('dept:add')")
    public CommonResult create(@Validated(SysDepartment.Save.class) @RequestBody SysDepartment department){
        redisTemplate.delete(Constant.REDIS_PREFIX + Constant.ALL_TREE);
        return CommonResult.success(departmentService.save(department));
    }


    @ApiOperation("修改部门")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('dept:edit')")
    public CommonResult<Boolean> update(@Validated(SysDepartment.Update.class) @RequestBody SysDepartment department){
        redisTemplate.delete(Constant.REDIS_PREFIX + Constant.ALL_TREE);
        return CommonResult.success(departmentService.updateById(department));
    }


    @ApiOperation("删除部门")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('dept:del')")
    public CommonResult delete(@RequestParam Long id){
        return CommonResult.success(departmentService.deleteById(id));
    }
}
