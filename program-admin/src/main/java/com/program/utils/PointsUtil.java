package com.program.utils;

import com.program.model.dict.DictConstant;

/**
 * @author linxf
 * @date 2024/8/18
 */

public class PointsUtil {

    /**
     * 根据连续签到天数计算积分。
     * 规则：连续签到天数等于获得的积分，最高为15
     * @param consecutiveDays 连续签到的天数
     * @return 对应的积分
     */
    public static int calculatePoints(int consecutiveDays) {
        if (consecutiveDays>=DictConstant.MAX_SIGN_IN_POINTS){
            return DictConstant.MAX_SIGN_IN_POINTS;
        }
        return consecutiveDays != 0 ? consecutiveDays :1 ;
    }

}
