package com.program.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserInfoDto {
    private Integer id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 旧密码
    private String oldPassword;
    // 真实姓名
    private String trueName;
    // 手机号
    private String phone;
    // 手机验证码
    private String codePhone;
}
