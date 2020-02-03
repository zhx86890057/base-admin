package com.zteng.moraleducation.security;

import com.zteng.moraleducation.pojo.entity.SysMenu;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.pojo.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 */
public class SysUserDetails implements UserDetails {

    private SysUser sysUser;
    private List<SysRole> roleList;
    private List<SysMenu> menuList;

    public SysUserDetails(SysUser sysUser, List<SysRole> roleList, List<SysMenu> menuList) {
        this.sysUser = sysUser;
        this.roleList = roleList;
        this.menuList = menuList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        List<SimpleGrantedAuthority> roleGrantList =
                roleList.stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s.getName())).collect(Collectors.toList());
        List<SimpleGrantedAuthority> authorityList =
                menuList.stream().filter(s -> StringUtils.isNotBlank(s.getResource())).map(s -> new SimpleGrantedAuthority(s.getResource())).collect(Collectors.toList());
        roleGrantList.addAll(authorityList);
        return roleGrantList;
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return 1 == sysUser.getEnabled();
    }
}
