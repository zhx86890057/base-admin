package com.zteng.moraleducation.pojo.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserParam {
    @ApiModelProperty("根据姓名或者手机号模糊查询")
    private String name;
    @ApiModelProperty("状态：1启用、0禁用")
    private Integer enabled;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
