package com.zteng.moraleducation.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zteng.moraleducation.pojo.entity.SysPermission;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.service.ISysPermissionService;
import com.zteng.moraleducation.service.ISysRoleService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private Map<String, List<ConfigAttribute>> resources;
    private ISysPermissionService permissionService;
    private ISysRoleService roleService;

    public CustomSecurityMetadataSource(ISysPermissionService permissionService, ISysRoleService roleService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        loadAuthorityResources();
    }

    private void loadAuthorityResources() {
        // 此处在创建时从数据库中初始化权限数据
        // 将权限与资源数据整理成 Map<resource, List<Authority>> 的形式
        // 注意：加载URL资源时，需要对资源进行排序，要由精确到粗略进行排序，让精确的URL优先匹配,在数据库中排序
        resources = new LinkedHashMap<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByAsc("sort");
        List<SysPermission> permissionList = permissionService.list(wrapper);
        if(!CollectionUtils.isEmpty(permissionList)){
            List<Long> permissionIdList = permissionList.stream().map(s -> s.getId()).collect(Collectors.toList());
            Map<Long, List<SysRole>> mapByPermissionIds = roleService.getMapByPermissionIds(permissionIdList);
            if(!CollectionUtils.isEmpty(mapByPermissionIds)){
                permissionList.forEach(s -> {
                    if(mapByPermissionIds.containsKey(s.getId())){
                        List<SysRole> roleDTOList = mapByPermissionIds.get(s.getId());
                        List<ConfigAttribute> authorityList =
                                roleDTOList.stream().map(r -> new SecurityConfig(r.getCode())).collect(Collectors.toList());
                        resources.put(s.getUrl(),authorityList);
                    }
                });
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        FilterInvocation fi = (FilterInvocation) object;
        for (Map.Entry<String, List<ConfigAttribute>> entry : resources.entrySet()) {
            String uri = entry.getKey();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
            if (requestMatcher.matches(fi.getHttpRequest())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}