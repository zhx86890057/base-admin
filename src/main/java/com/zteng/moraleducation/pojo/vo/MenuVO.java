package com.zteng.moraleducation.pojo.vo;

import com.zteng.moraleducation.pojo.entity.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MenuVO {
    @NotNull(groups = SysMenu.Update.class)
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "上级菜单ID")
    private Long pid;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "链接地址")
    private String path;

    @ApiModelProperty(value = "资源权限")
    private String resource;

    @ApiModelProperty(value = "类型")
    private Integer type;

    private List<MenuVO> children;
}
