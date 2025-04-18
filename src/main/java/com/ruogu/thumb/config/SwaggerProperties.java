package com.ruogu.thumb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/***
 *@author ruogu
 *@create 2025/4/18 14:38
 **/
@Data
@Component
@ConfigurationProperties(prefix = "info")
public class SwaggerProperties {


    private String title = "ruogu-like";

    private String description = "亿级流量点赞系统";

    private String version = "1.0.0";

    private String contactName = "ruogu";

    private String contactEmail = "1687438992@qq.com";
}
