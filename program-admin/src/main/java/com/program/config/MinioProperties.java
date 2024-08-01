package com.program.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



/**
 * @author linxf
 * @date 2024/7/26
 */
@Data
@Configuration
@ConfigurationProperties(prefix="program.config.minio") //读取节点
public class MinioProperties {

    // minio服务器的域名和端口
    private String endpointUrl;
    // 访问minio服务器的用户名
    private String accessKey;
    // 访问minio服务器的密码
    private String secretKey;
    // 存储桶名称
    private String bucketName;
}
