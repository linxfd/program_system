package com.program.model.vo;

import lombok.Data;

/**
 * @author linxf
 * @date 2024/9/12
 */
@Data
public class PointsVo {

    /**
     * 是否兑换
     */
    private Integer isRedeemed;

    /**
     * 积分数量
     */
    private Integer pointsNumber;
}
