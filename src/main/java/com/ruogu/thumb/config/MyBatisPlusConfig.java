package com.ruogu.thumb.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/***
 *@author ruogu
 *@create 2025/4/18 11:39
 **/
@Configuration
@MapperScan("com.ruogu.thumb.mapper")
public class MyBatisPlusConfig {
    // @Bean
    // public MybatisPlusInterceptor mybatisPlusInterceptor() {
    //     MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    //     // 分页插件
    //     interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    //     return interceptor;
    // }

}