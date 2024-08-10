package com.program.utils.date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author linxf
 * @date 2024/8/9
 * 日期格式化枚举
 */
@Getter
@AllArgsConstructor
public enum DateFormatEnum {

    SDF_YEAR("yyyy"),
    SDF_YMD("yyyyMMdd"),
    SDF_Y_M_D("yyyy-MM-dd"),
    SDF_Y_M_D_H_M("yyyy-MM-dd HH:mm"),
    SDF_Y_M_D_H_M_S("yyyy-MM-dd HH:mm:ss"),
    SDF_YMDHMS("yyyyMMddHHmmss"),
    SDF_HM("HH:mm"),
    ;

    private final String formateStr;
}
