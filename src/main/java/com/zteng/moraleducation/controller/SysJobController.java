package com.zteng.moraleducation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zteng.moraleducation.common.CommonResult;
import com.zteng.moraleducation.pojo.entity.SysJob;
import com.zteng.moraleducation.pojo.vo.JobVO;
import com.zteng.moraleducation.service.ISysJobService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 岗位表 前端控制器
 * </p>
 *
 * @author 
 * @since 2020-01-17
 */
@Controller
@RequestMapping("/job")
public class SysJobController {
    @Autowired
    private ISysJobService jobService;

    @ApiOperation("查询岗位列表")
    @GetMapping("/list")
    public CommonResult<IPage<JobVO>> getJobs(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(defaultValue = "1") Integer pageNo,
                                              @RequestParam(defaultValue = "10")Integer pageSize){
        return CommonResult.success(null);
    }


    @ApiOperation("新增岗位")
    @PostMapping("/create")
    public CommonResult create(@Validated(SysJob.Save.class) @RequestBody SysJob sysJob){
        return CommonResult.success(jobService.save(sysJob));
    }


    @ApiOperation("修改岗位")
    @PutMapping("/update")
    public CommonResult<Boolean> update(@Validated(SysJob.Update.class) @RequestBody SysJob sysJob){
        return CommonResult.success(jobService.updateById(sysJob));
    }


    @ApiOperation("删除部门")
    @DeleteMapping
    public CommonResult delete(@RequestParam Long id){
        return CommonResult.success(jobService.removeById(id));
    }
}
