package com.program.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.program.exception.CommonErrorCode;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author linxf
 * @date 2024/8/17
 * 关于时间计算的工具类
 */
public class DateMyUtil {
    /**
     * 获取当前时间与创建时间间隔天数
     * @param date 创建时间
     * @return
     */
    public static Long getCurrentDate(Date date) {
        LocalDate createLocalDate  = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentLocalDate  = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(createLocalDate, currentLocalDate);
        return daysBetween;
    }

    /**
     * 获取日期
     *
     * @param dateStr
     * @return
     */
    public static Date getDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return new Date();
        }
        try {
            return DateUtil.parseDate(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("请传入yyyy-MM-dd的日期格式");
        }
    }

}
