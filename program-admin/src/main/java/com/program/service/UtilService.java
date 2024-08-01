package com.program.service;

/**
 * @author linxf
 * @date 2024/8/1
 */
public interface UtilService {

    /**
     * 发送验证码
     * @param phone 手机号
     */
    void sendValidateCode(String phone);
}
