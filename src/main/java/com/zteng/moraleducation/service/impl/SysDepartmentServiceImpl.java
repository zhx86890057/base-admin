package com.zteng.moraleducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.common.CommonException;
import com.zteng.moraleducation.constant.Constant;
import com.zteng.moraleducation.mapper.SysDepartmentMapper;
import com.zteng.moraleducation.pojo.entity.*;
import com.zteng.moraleducation.pojo.vo.DepartVO;
import com.zteng.moraleducation.pojo.vo.UserVO;
import com.zteng.moraleducation.service.ISysDepartmentService;
import com.zteng.moraleducation.service.ISysJobService;
import com.zteng.moraleducation.service.ISysRoleDeptRelationService;
import com.zteng.moraleducation.service.ISysUserService;
import com.zteng.moraleducation.utils.BeanCopierUtil;
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
    @Autowired
    private ISysRoleDeptRelationService roleDeptRelationService;

    private final String[] scopeType = {"全部", "本级", "自定义"};

    @Override
    public List<DepartVO> buildTree(List<SysDepartment> departments) {
        List<DepartVO> list = departments.stream().map(s -> {
            DepartVO departVO = new DepartVO();
            BeanCopierUtil.copyProperties(s, departVO);
            return departVO;
        }).collect(Collectors.toList());
        List<DepartVO> trees = new ArrayList<>();
        for (DepartVO departVO : list) {
            if (departVO.getPid() == 0) {
                trees.add(departVO);
            }
            for (DepartVO it : list) {
                if (it.getPid() == departVO.getId()) {
                    if (departVO.getChildren() == null) {
                        departVO.setChildren(new ArrayList<>());
                    }
                    departVO.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    @Override
    public boolean deleteById(Long id) {
        QueryWrapper<SysDepartment> wrapper = new QueryWrapper();
        wrapper.eq("pid", id);
        int deptCount = this.count(wrapper);
        if (deptCount > 0) {
            throw new CommonException("请先删除下一级部门");
        }
        QueryWrapper<SysJob> jobWrapper = new QueryWrapper();
        jobWrapper.eq("dept_id", id);
        int jobCount = jobService.count(jobWrapper);
        if (jobCount > 0) {
            throw new CommonException("请先删除该部门下的岗位");
        }
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("dept_id", id);
        int userCount = userService.count(userWrapper);
        if (userCount > 0) {
            throw new CommonException("请先删除该部门下的人员");
        }
        redisTemplate.delete(Constant.REDIS_PREFIX + Constant.ALL_DEPT);
        return this.removeById(id);
    }

    public Map<Long, SysDepartment> getMapByIds(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            Collection<SysDepartment> sysDepartments = this.listByIds(ids);
            return sysDepartments.stream().collect(Collectors.toMap(SysDepartment::getId, Function.identity()));
        }
        return Collections.emptyMap();
    }

    public List<Long> getByRoleId(Long roleId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("role_Id", roleId);
        List<SysRoleDeptRelation> list = roleDeptRelationService.list(wrapper);
        List<Long> deptList = list.stream().map(s -> s.getDeptId()).collect(Collectors.toList());
        return deptList;
    }

    public List<SysDepartment> getByPid(Long pid) {
        Map<Long, List<SysDepartment>> map;
        if (redisTemplate.hasKey(Constant.REDIS_PREFIX + Constant.ALL_DEPT)) {
            map = (Map<Long, List<SysDepartment>>) redisTemplate.opsForValue().get(Constant.REDIS_PREFIX + Constant.ALL_DEPT);
        } else {
            List<SysDepartment> list = this.list();
            map = list.stream().collect(Collectors.groupingBy(SysDepartment::getPid));
            redisTemplate.opsForValue().set(Constant.REDIS_PREFIX + Constant.ALL_DEPT, map, 1, TimeUnit.DAYS);
        }
        if (map.containsKey(pid)) {
            return map.get(pid);
        }
        return Collections.emptyList();
    }

    /**
     * 递归查询子节点
     * @param deptList
     * @return
     */
    private List<SysDepartment> getDeptChildren(List<SysDepartment> deptList) {
        List<SysDepartment> list = new ArrayList<>();
        deptList.forEach(dept -> {
            if (dept != null && dept.getStatus() == 1) {
                List<SysDepartment> depts = this.getByPid(dept.getId());
                if (CollectionUtils.isNotEmpty(depts)) {
                    list.addAll(getDeptChildren(depts));
                }
                list.add(dept);
            }
        });
        return list;
    }

    public Set<Long> getDeptIdsByUser(String username) {

        UserVO user = userService.getUserInfo(username);
        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();
        //用户角色
        List<SysRole> roleList = user.getRoleList();

        for (SysRole role : roleList) {
            if (scopeType[0].equals(role.getDataScope())) {
                return new HashSet<>();
            }
            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(user.getDept().getId());
            }
            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                List<Long> deptIdList = this.getByRoleId(role.getId());
                for (Long id : deptIdList) {
                    deptIds.add(id);
                    List<SysDepartment> deptChildren = this.getByPid(id);
                    if (CollectionUtils.isNotEmpty(deptChildren)) {
                        List<SysDepartment> children = this.getDeptChildren(deptChildren);
                        deptIds.addAll(children.stream().map(s -> s.getId()).collect(Collectors.toList()));
                    }
                }
            }
        }
        return deptIds;
    }

    public List<DepartVO> getChildrenByDeptId(Long deptId) {
        List<DepartVO> departVOS = this.buildTree(this.list());
        return this.recursion(deptId, departVOS);
    }

    private List<DepartVO> recursion(Long deptId, List<DepartVO> departVOS) {
        if (CollectionUtils.isNotEmpty(departVOS)) {
            for (DepartVO departVO : departVOS) {
                if (departVO.getId() == deptId) {
                    return departVO.getChildren();
                }
                return recursion(deptId, departVO.getChildren());
            }
        }
        return Collections.emptyList();
    }
}
