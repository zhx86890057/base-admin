package com.zteng.moraleducation.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * mybatis-plus 配置
 * </p>
 *
 * @package: com.xkcoding.orm.mybatis.plus.config
 * @description: mybatis-plus 配置
 * @author: yangkai.shen
 * @date: Created in 2018/11/8 17:29
 * @copyright: Copyright (c) 2018
 * @version: V1.0
 * @modified: yangkai.shen
 */
@Configuration
@MapperScan(basePackages = {"com.zteng.moraleducation.mapper"})
//spring-boot 会自动配置事务，相关的配置在 org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
//@EnableTransactionManagement
public class MybatisPlusConfig {
    /**
     * 性能分析拦截器，不建议生产使用
     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor(){
//        return new PerformanceInterceptor();
//    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 逻辑删除，在字段上加 @TableLogic
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }
}
