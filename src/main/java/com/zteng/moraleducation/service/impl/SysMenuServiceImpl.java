package com.zteng.moraleducation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zteng.moraleducation.mapper.SysMenuMapper;
import com.zteng.moraleducation.pojo.entity.SysMenu;
import com.zteng.moraleducation.pojo.vo.MenuVO;
import com.zteng.moraleducation.service.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<MenuVO> buildTree(List<MenuVO> list) {
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
}
