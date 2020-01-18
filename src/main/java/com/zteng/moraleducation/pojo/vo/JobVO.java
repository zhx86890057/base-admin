package com.zteng.moraleducation.pojo.vo;

import com.zteng.moraleducation.pojo.entity.SysJob;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JobVO {
    @NotNull(groups = SysJob.Update.class)
    private Long id;

    @ApiModelProperty(value = "岗位名称")
    private String name;

    @ApiModelProperty(value = "岗位排序")
    private Integer sort;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "状态 0 禁用  1可用")
    private Byte status;
}
