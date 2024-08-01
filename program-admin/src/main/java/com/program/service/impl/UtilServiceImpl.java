package com.program.service.impl;

import com.program.config.properties.ValidateCodeProperties;
import com.program.service.UtilService;
import com.program.utils.HttpUtils;
import com.program.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author linxf
 * @date 2024/8/1
 */
@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {

    private final RedisUtil redisUtil;

    @Autowired
    private ValidateCodeProperties validateCodeProperties ;

    @Override
    public void sendValidateCode(String phone) {
        // 判断是否已经发送验证码
        String code = (String)redisUtil.get("phone:code:" + phone);
        if(StringUtils.hasText(code)) {
            return;
        }
        // 生成验证码
        String validateCode = RandomStringUtils.randomNumeric(4);      // 生成验证码
        // 将验证码保存到redis中, 设置过期时间,5分钟
        redisUtil.set("phone:code:" + phone , validateCode , 5 , TimeUnit.MINUTES);

        // 调用短信发送方法，该方法可以直接从短信服务api平台直接复制修改
        this.sendSms(phone , validateCode) ;
    }


    // 发送短信方法，该方法可以直接从短信服务api平台直接复制修改
    public void sendSms(String phone, String validateCode) {

        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = validateCodeProperties.getAppCode();
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:"+validateCode);
        bodys.put("template_id", validateCodeProperties.getTemplateId());
        bodys.put("phone_number", phone);

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
