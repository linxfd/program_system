package com.program.model.vo;

import lombok.Getter;

/**
 * @version 1.0
 */
@Getter
public enum CommonResultEnum {

    SUCCESS(200 , "操作成功"),
    SUCCESS_LOGIN(200 , "登录成功"),
    SUCCESS_LOGOUT(200 , "退出成功"),
    SUCCESS_REGISTER(200 , "注册成功"),
    SUCCESS_UPDATE(200 , "更新成功"),
    SUCCESS_DELETE(200 , "删除成功"),
    SUCCESS_ADD(200 , "添加成功"),
    SUCCESS_QUERY(200 , "查询成功"),
    SUCCESS_OBTAIN(200 , "获取成功"),
    LOGIN_ERROR(201 , "用户名或者密码错误"),
    VALIDATECODE_ERROR(202 , "验证码错误") ,
    LOGIN_AUTH(208 , "用户未登录"),
    USER_NAME_IS_EXISTS(209 , "用户名已经存在"),
    ROLE_NAME_IS_EXISTS(209 , "角色名已经存在"),
    FAIL_DELETE(210 , "删除失败,还有用户赋值该角色"),
    PHONE_ERROR(210 , "手机号格式错误"),
    SYSTEM_ERROR(9999 , "您的网络有问题请稍后重试"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    ACCOUNT_STOP( 216, "账号已停用"),
    STOCK_LESS( 219, "库存不足"),
    EMAIL_ERROR(210 , "邮箱格式错误"),
            ;

    private Integer code ;      // 业务状态码
    private String message ;    // 响应消息

    private CommonResultEnum(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }
}
