package com.program.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "program.config.oss")
public class OSSConfig {

    public boolean enable = false;

    public String endpoint = "你的oss地址";

    public String accesskeyid = "oss认证key";

    public String accesskeysecret = "oss认证密钥";

    public String bucketname = "创建的bucket name";

    public String key = "images/upload/";

    public String host = "oss-cn-beijing.aliyuncs.com";

}
