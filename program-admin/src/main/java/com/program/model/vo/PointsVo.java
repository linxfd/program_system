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
     * 兑换所需积分数量
     */
    private Integer pointsNumber;

    /**
     * 该用户的当前拥护的总积分
     */
    private Integer pointsTotal;
}
