package com.zteng.moraleducation.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 通用字段填充
 * </p>
 *
 * @package: com.xkcoding.orm.mybatis.plus.config
 * @description: 通用字段填充
 * @author: yangkai.shen
 * @date: Created in 2018/11/8 17:40
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@Slf4j
@Component
public class CommonFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setInsertFieldValByName("createTime", new Date(), metaObject);
        this.setInsertFieldValByName("delFlag", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setUpdateFieldValByName("modifyTime", new Date(), metaObject);
    }
}
