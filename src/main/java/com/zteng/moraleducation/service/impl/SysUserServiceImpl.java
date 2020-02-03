package com.zteng.moraleducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.common.CommonException;
import com.zteng.moraleducation.common.ResultCode;
import com.zteng.moraleducation.constant.Constant;
import com.zteng.moraleducation.mapper.SysUserMapper;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.entity.SysJob;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.pojo.entity.SysUser;
import com.zteng.moraleducation.pojo.param.UserParam;
import com.zteng.moraleducation.pojo.vo.UserVO;
import com.zteng.moraleducation.service.*;
import com.zteng.moraleducation.utils.BeanCopierUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ISysDepartmentService departmentService;
    @Autowired
    private ISysJobService jobService;

    public UserVO getUserInfo(String username){
        if(redisTemplate.hasKey(Constant.USER_PREFIX + username)){
            return (UserVO) redisTemplate.opsForValue().get(Constant.USER_PREFIX + username);
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        SysUser sysUser = this.getOne(wrapper);
        if(sysUser == null){
            throw new CommonException(ResultCode.NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);

        SysDepartment department = departmentService.getById(sysUser.getDeptId());
        SysJob job = jobService.getById(sysUser.getJobId());
        List<SysRole> sysRoles = roleService.listByUserId(sysUser.getId());
        userVO.setDept(department).setJob(job).setRoleList(sysRoles);
        redisTemplate.opsForValue().set(Constant.USER_PREFIX + username, userVO, 1L, TimeUnit.DAYS);

        return userVO;
    }

    public IPage<UserVO> pageList(String username, UserParam userParam){
        Set<Long> deptIds = departmentService.getDeptIdsByUser(username);
        QueryWrapper<SysUser> wrapper = new QueryWrapper();
        wrapper.eq(userParam.getEnabled() != null, "status", userParam.getEnabled());
        wrapper.and(StringUtils.isNotBlank(userParam.getName()),
                i-> i.like("nick_name", userParam.getName()).or().like("phone", userParam.getName()));
        wrapper.in(CollectionUtils.isNotEmpty(deptIds), "dept_id", deptIds);
        IPage page = this.page(new Page<>(userParam.getPageNo(), userParam.getPageSize()), wrapper);
        if(page.getTotal() == 0){
            return page;
        }
        List<SysUser> records = page.getRecords();
        List<UserVO> userVOS = records.stream().map(s -> {
            UserVO userVO = new UserVO();
            BeanCopierUtil.copyProperties(s, userVO);
            return userVO;
        }).collect(Collectors.toList());

        List<Long> userIds = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        List<Long> jobIdList = new ArrayList<>();
        records.forEach(s -> {
            userIds.add(s.getId());
            deptIdList.add(s.getDeptId());
            jobIdList.add(s.getJobId());
        });
        Map<Long, List<SysRole>> userMap = roleService.getMapByUserIds(userIds);
        Map<Long, SysDepartment> deptMap = departmentService.getMapByIds(deptIdList);
        Map<Long, SysJob> jobMap = jobService.getMapByIds(jobIdList);

        userVOS.forEach(s -> {
            s.setRoleList(userMap.get(s.getId()));
            s.setDept(deptMap.get(s.getDept()));
            s.setJob(jobMap.get(s.getJob()));
        });
        page.setRecords(userVOS);
        return page;
    }

    public void logout(String username){
        redisTemplate.delete(Constant.USER_PREFIX + username);
    }
}
