package com.program.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author linxf
 * @date 2024/8/17
 */
public interface SignService {

    // 签到
    int doSign(String date, HttpServletRequest request);

    // 签到记录
    Long getSignCount(String date, HttpServletRequest request);

    // 签到记录
    Map<String, Boolean> getSignInfo(String dateStr,HttpServletRequest request);
}
