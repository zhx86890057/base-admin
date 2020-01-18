package com.zteng.moraleducation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.common.CommonException;
import com.zteng.moraleducation.constant.Constant;
import com.zteng.moraleducation.mapper.SysDepartmentMapper;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.entity.SysJob;
import com.zteng.moraleducation.pojo.entity.SysUser;
import com.zteng.moraleducation.pojo.vo.DepartVO;
import com.zteng.moraleducation.service.ISysDepartmentService;
import com.zteng.moraleducation.service.ISysJobService;
import com.zteng.moraleducation.service.ISysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-01-17
 */
@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements ISysDepartmentService {
    @Autowired
    private ISysJobService jobService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<DepartVO> buildTree(List<DepartVO> list) {
        List<DepartVO> trees = new ArrayList<>();
        for (DepartVO departVO: list){
            if(departVO.getPid() == 0){
                trees.add(departVO);
            }
            for (DepartVO it: list){
                if(it.getPid() == departVO.getId()){
                    if(departVO.getChildren() == null){
                        departVO.setChildren(new ArrayList<>());
                    }
                    departVO.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    public List<DepartVO> buildAllTree() {
        if(redisTemplate.hasKey(Constant.REDIS_PREFIX + Constant.ALL_TREE)){
            return (List<DepartVO>) redisTemplate.opsForValue().get(Constant.REDIS_PREFIX + Constant.ALL_TREE);
        }
        List<SysDepartment> departmentList = this.list();
        List<DepartVO> list = JSON.parseArray(JSON.toJSONString(departmentList), DepartVO.class);
        List<DepartVO> departVOS = this.buildTree(list);
        redisTemplate.opsForValue().set(Constant.REDIS_PREFIX + Constant.ALL_TREE, departVOS, 1, TimeUnit.DAYS);
        return departVOS;
    }

    @Override
    public boolean deleteById(Long id) {
        QueryWrapper<SysDepartment> wrapper = new QueryWrapper();
        wrapper.eq("pid", id);
        int deptCount = this.count(wrapper);
        if(deptCount > 0){
            throw new CommonException("请先删除下一级部门");
        }
        QueryWrapper<SysJob> jobWrapper = new QueryWrapper();
        jobWrapper.eq("dept_id", id);
        int jobCount = jobService.count(jobWrapper);
        if(jobCount > 0){
            throw new CommonException("请先删除该部门下的岗位");
        }
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("dept_id", id);
        int userCount = userService.count(userWrapper);
        if(userCount > 0){
            throw new CommonException("请先删除该部门下的人员");
        }
        redisTemplate.delete(Constant.REDIS_PREFIX + Constant.ALL_TREE);
        return this.removeById(id);
    }

    public Map<Long, SysDepartment> getMapByIds(List<Long> ids){
        if(CollectionUtils.isNotEmpty(ids)){
            Collection<SysDepartment> sysDepartments = this.listByIds(ids);
            return sysDepartments.stream().collect(Collectors.toMap(SysDepartment::getId, Function.identity()));
        }
        return Collections.emptyMap();
    }

    public List getChildrenByDeptId(Long deptId){
        List<DepartVO> departVOS = this.buildAllTree();
        return this.recursion(deptId, departVOS);
    }

    private List recursion(Long deptId, List<DepartVO> departVOS){
        if(CollectionUtils.isNotEmpty(departVOS)){
            for(DepartVO departVO: departVOS){
                if(departVO.getId() == deptId){
                    return departVO.getChildren();
                }
                return recursion(deptId, departVO.getChildren());
            }
        }
        return Collections.emptyList();
    }
}
