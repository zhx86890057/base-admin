package com.zteng.moraleducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.mapper.SysJobMapper;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.entity.SysJob;
import com.zteng.moraleducation.pojo.vo.JobVO;
import com.zteng.moraleducation.service.ISysDepartmentService;
import com.zteng.moraleducation.service.ISysJobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 岗位表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-01-17
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {
    @Autowired
    private ISysDepartmentService departmentService;

    @Override
    public IPage<JobVO> pageList(String name, Integer status, Integer pageNo, Integer pageSize) {
        QueryWrapper<SysJob> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(name), "name", name);
        wrapper.eq(status != null, "status", status);
        IPage page = this.page(new Page<>(pageNo, pageSize), wrapper);
        if(page.getTotal() == 0){
            return page;
        }
        List<SysJob> records = page.getRecords();
        List<Long> departIdList = records.stream().map(s -> s.getDeptId()).collect(Collectors.toList());
        Map<Long, SysDepartment> deptMap = departmentService.getMapByIds(departIdList);

        List<JobVO> jobVOS = records.stream().map(s -> {
            JobVO jobVO = new JobVO();
            BeanUtils.copyProperties(s, jobVO);
            if (deptMap.containsKey(s.getDeptId())) {
                jobVO.setDeptName(deptMap.get(s.getDeptId()).getName());
            }
            return jobVO;
        }).collect(Collectors.toList());
        page.setRecords(jobVOS);
        return page;
    }
}
