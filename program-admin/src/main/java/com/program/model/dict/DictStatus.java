package com.program.model.dict;

/**
 * @author linxf
 * @date 2024/9/8
 */
public interface DictStatus {
    /**
     * 正常，启用
     */
    String NORMAL = "1";
    /**
     * 禁用
     */
    String DISABLE = "2";

    /**
     * 未发布
     */
    Integer UNPUBLISHED = 1;

    /**
     * 已发布
     */
    Integer PUBLISHED = 2;

    /**
     * 未审核
     */
    Integer UNAUDITED = 1;

    /**
     * 审核不通过
     */
    Integer UNPASS = 2;

    /**
     * 审核通过
     */
    Integer PASS = 3;

    /**
     * 公共课程
     */
    Integer PUBLIC_COURSE = 1;

    /**
     * 积分课程
     */
    Integer POINTS_COURSE = 2;

    /**
     * 未兑换
     */
    Integer NOT_REDEEMED = 1;
    /**
     * 已兑换
     */
    Integer REDEEMED = 2;

    /**
     * 兑换课程
     */
    Integer COURSE = 1;

    /**
     * 兑换题库
     */
    Integer EXAM = 2;

}
