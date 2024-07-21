package com.program.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    @Builder.Default
    private Integer code = 200;

    private String message;

    private T data;


    // 成功 200
    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }


    // 构造
    public static <T> CommonResult<T> build(T body, Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 通过枚举构造CommonResult对象
    public static <T> CommonResult build(T body , CommonResultEnum commonResultEnum) {
        return build(body , commonResultEnum.getCode() , commonResultEnum.getMessage()) ;
    }
}
