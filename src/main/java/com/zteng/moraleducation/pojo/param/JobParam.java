package com.zteng.moraleducation.pojo.param;

import lombok.Data;

@Data
public class JobParam {
    private String name;
    private Integer status;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
