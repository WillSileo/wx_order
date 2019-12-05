package com.sileo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "projectUrl")
@Data
public class ProjectUrlConfig {
    /*
    微信公众平台授权url
     */
    private String wechatMpAuthorize;
    /*
    微信开放平台授权url
     */
    private String wechatOpenAuthorize;
    /*
    点餐系统
     */
    private String sell;
}
