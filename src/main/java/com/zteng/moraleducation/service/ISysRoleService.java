package com.zteng.moraleducation.service;

import com.zteng.moraleducation.pojo.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
public interface ISysRoleService extends IService<SysRole> {
    List<SysRole> listByUserId(Long userId);

    /**
     * 根据userid查询角色列表
     * @param userIds
     * @return
     */
    Map<Long, List<SysRole>> getMapByUserIds(List<Long> userIds);

    /**
     * 根据permissionId查询拥有的角色列表
     * @param permissionIds url路径id
     * @return
     */
    Map<Long, List<SysRole>> getMapByPermissionIds(List<Long> permissionIds);
}
