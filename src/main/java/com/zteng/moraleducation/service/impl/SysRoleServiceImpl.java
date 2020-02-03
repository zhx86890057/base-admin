package com.zteng.moraleducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.mapper.SysRoleMapper;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.pojo.entity.SysRolePermissionRelation;
import com.zteng.moraleducation.pojo.entity.SysUserRoleRelation;
import com.zteng.moraleducation.service.ISysRolePermissionRelationService;
import com.zteng.moraleducation.service.ISysRoleService;
import com.zteng.moraleducation.service.ISysUserRoleRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private ISysUserRoleRelationService userRoleRelationService;
    @Autowired
    private ISysRolePermissionRelationService rolePermissionRelationService;

    public List<SysRole> listByUserId(Long userId){
        QueryWrapper userRoleWrapper = new QueryWrapper();
        userRoleWrapper.eq("user_id", userId);
        List<SysUserRoleRelation> userRoleList = userRoleRelationService.list(userRoleWrapper);
        List<Long> roleIds = userRoleList.stream().map(s -> s.getRoleId()).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(roleIds)){
            QueryWrapper roleWrapper = new QueryWrapper();
            roleWrapper.in("id", roleIds);
            return this.list(roleWrapper);
        }
        return Collections.emptyList();
    }

    public Map<Long, List<SysRole>> getMapByUserIds(List<Long> userIds){
        QueryWrapper userRoleWrapper = new QueryWrapper();
        userRoleWrapper.in("user_id", userIds);
        List<SysUserRoleRelation> userRoleList = userRoleRelationService.list(userRoleWrapper);
        if(CollectionUtils.isNotEmpty(userRoleList)){
            return Collections.emptyMap();
        }
        Map<Long, List<SysUserRoleRelation>> userIdMap = userRoleList.stream().collect(Collectors.groupingBy(SysUserRoleRelation::getUserId));

        List<Long> roleIds = userRoleList.stream().map(s -> s.getRoleId()).collect(Collectors.toList());
        QueryWrapper roleWrapper = new QueryWrapper();
        roleWrapper.in("id", roleIds);
        List<SysRole> roleList = this.list(roleWrapper);
        Map<Long, SysRole> roldIdMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, Function.identity()));

        //userid 对应角色列表
        Map<Long, List<SysRole>> userRoleMap = new HashMap<>();
        userIdMap.forEach((k,v)->{
            List<SysRole> roles = v.stream().map(s -> roldIdMap.get(s.getRoleId())).filter(f -> f != null).collect(Collectors.toList());
            userRoleMap.put(k, roles);
        });
        return userRoleMap;
    }

    public Map<Long, List<SysRole>> getMapByPermissionIds(List<Long> permissionIds){
        QueryWrapper userRoleWrapper = new QueryWrapper();
        userRoleWrapper.in("permission_id", permissionIds);
        List<SysRolePermissionRelation> rolePermissionList = rolePermissionRelationService.list(userRoleWrapper);
        if(CollectionUtils.isNotEmpty(rolePermissionList)){
            return Collections.emptyMap();
        }
        Map<Long, List<SysRolePermissionRelation>> permissionIdMap =
                rolePermissionList.stream().collect(Collectors.groupingBy(SysRolePermissionRelation::getPermissionId));

        List<Long> roleIds = rolePermissionList.stream().map(s -> s.getRoleId()).collect(Collectors.toList());
        QueryWrapper roleWrapper = new QueryWrapper();
        roleWrapper.in("id", roleIds);
        List<SysRole> roleList = this.list(roleWrapper);
        Map<Long, SysRole> roldIdMap = roleList.stream().collect(Collectors.toMap(SysRole::getId, Function.identity()));

        //permissionId 对应角色列表
        Map<Long, List<SysRole>> rolePermissionMap = new HashMap<>();
        permissionIdMap.forEach((k,v)->{
            List<SysRole> roles = v.stream().map(s -> roldIdMap.get(s.getRoleId())).filter(f -> f != null).collect(Collectors.toList());
            rolePermissionMap.put(k, roles);
        });
        return rolePermissionMap;
    }
}
