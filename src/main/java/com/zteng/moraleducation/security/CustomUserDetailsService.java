package com.zteng.moraleducation.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zteng.moraleducation.pojo.entity.SysMenu;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.pojo.entity.SysUser;
import com.zteng.moraleducation.service.ISysMenuService;
import com.zteng.moraleducation.service.ISysRoleService;
import com.zteng.moraleducation.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 提供到数据库查询该用户的权限信息
        // 关于角色和权限的转换关系在此处处理，根据用户与角色的关系、角色与权限的关系，
        // 将用户与权限的管理整理出来
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        SysUser user = userService.getOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        List<SysRole> sysRoles = roleService.listByUserId(user.getId());
        List<Long> roleIds = sysRoles.stream().map(s -> s.getId()).collect(Collectors.toList());
        List<SysMenu> sysMenus = menuService.listByRoleIds(roleIds, null);
        return new SysUserDetails(user, sysRoles, sysMenus);
    }
}
