package com.zteng.moraleducation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zteng.moraleducation.pojo.entity.SysMenu;
import com.zteng.moraleducation.pojo.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
public interface ISysMenuService extends IService<SysMenu> {
    List<MenuVO> buildTree(List<SysMenu> list);

    /**
     * 根据roleIds查询菜单
     * @param roleIds
     * @return
     */
    List<SysMenu> listByRoleIds(List<Long> roleIds, String name);

    /**
     * 查询用户能看到的菜单
     * @param username
     * @param name
     * @return
     */
    List<SysMenu> listByUsername(String username, String name);
}
