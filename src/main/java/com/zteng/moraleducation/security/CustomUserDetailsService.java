package com.zteng.moraleducation.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 提供到数据库查询该用户的权限信息
        // 关于角色和权限的转换关系在此处处理，根据用户与角色的关系、角色与权限的关系，
        // 将用户与权限的管理整理出来
        return null;
    }
}
