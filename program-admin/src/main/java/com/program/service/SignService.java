package com.program.service;

import com.program.model.vo.TokenVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author linxf
 * @date 2024/8/17
 */
public interface SignService {

    // 签到
    Map<String, Object> doSign(String date, HttpServletRequest request);

    // 签到记录
    Long getSignCount(String date, HttpServletRequest request);

    // 签到记录
    Map<String, Boolean> getSignInfo(String dateStr,HttpServletRequest request);

    // 统计连续签到的次数
    int getContinuousSignCount(Integer userId, Date date) ;
}
