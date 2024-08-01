package com.program.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author linxf
 * @date 2024/8/1
 */
@Data
@Configuration
@ConfigurationProperties(prefix="program.config.validate-code") //读取节点
public class ValidateCodeProperties {
    // 验证码配置
    private String appCode;
    private String appSecret;
    private String templateId;

}
