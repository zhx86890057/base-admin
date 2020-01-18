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
    List<MenuVO> buildTree(List<MenuVO> list);
}
