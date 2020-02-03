package com.zteng.moraleducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.mapper.SysMenuMapper;
import com.zteng.moraleducation.pojo.entity.SysMenu;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.pojo.entity.SysRoleMenuRelation;
import com.zteng.moraleducation.pojo.vo.MenuVO;
import com.zteng.moraleducation.pojo.vo.UserVO;
import com.zteng.moraleducation.service.ISysMenuService;
import com.zteng.moraleducation.service.ISysRoleMenuRelationService;
import com.zteng.moraleducation.service.ISysUserService;
import com.zteng.moraleducation.utils.BeanCopierUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-01-13
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private ISysRoleMenuRelationService roleMenuRelationService;
    @Autowired
    private ISysUserService userService;

    public List<MenuVO> buildTree(List<SysMenu> sysMenus) {
        List<MenuVO> list = sysMenus.stream().map(s -> {
            MenuVO menuVO = new MenuVO();
            BeanCopierUtil.copyProperties(s, menuVO);
            return menuVO;
        }).collect(Collectors.toList());
        List<MenuVO> trees = new ArrayList<>();
        for (MenuVO menuVO: list){
            if(menuVO.getPid() == 0){
                trees.add(menuVO);
            }
            for (MenuVO it: list){
                if(it.getPid() == menuVO.getId()){
                    if(menuVO.getChildren() == null){
                        menuVO.setChildren(new ArrayList<>());
                    }
                    menuVO.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    public List<SysMenu> listByRoleIds(List<Long> roleIds, String name){
        if (CollectionUtils.isNotEmpty(roleIds)) {
            QueryWrapper roleMenuWrapper = new QueryWrapper();
            roleMenuWrapper.in("role_id", roleIds);
            roleMenuWrapper.like(StringUtils.isNotBlank(name),"name", name);
            List<SysRoleMenuRelation> roleMenuList = roleMenuRelationService.list(roleMenuWrapper);
            List<Long> menuIds = roleMenuList.stream().map(s -> s.getMenuId()).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(menuIds)){
                QueryWrapper menuWrapper = new QueryWrapper();
                menuWrapper.in("id", menuIds);
                return this.list(menuWrapper);
            }
        }
        return Collections.emptyList();
    }

    public List<SysMenu> listByUsername(String username, String name){
        UserVO userInfo = userService.getUserInfo(username);
        List<SysRole> sysRoles = userInfo.getRoleList();
        List<Long> roleIds = sysRoles.stream().map(s -> s.getId()).collect(Collectors.toList());
        List<SysMenu> sysMenus = this.listByRoleIds(roleIds, name);
        return sysMenus;
    }
}
