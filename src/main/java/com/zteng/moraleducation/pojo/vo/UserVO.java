package com.zteng.moraleducation.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zteng.moraleducation.pojo.entity.SysDepartment;
import com.zteng.moraleducation.pojo.entity.SysJob;
import com.zteng.moraleducation.pojo.entity.SysRole;
import com.zteng.moraleducation.pojo.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.management.relation.Role;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserVO {
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像路径")
    private String head;

    @ApiModelProperty(value = "状态：1启用、0禁用")
    private Integer enabled;

    @ApiModelProperty(value = "部门")
    private SysDepartment dept;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "岗位")
    private SysJob job;

    @ApiModelProperty(value = "最近一次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "未知 男 女")
    private String sex;

    private List<SysRole> roleList;

    private List<MenuVO> menuVOList;
}
