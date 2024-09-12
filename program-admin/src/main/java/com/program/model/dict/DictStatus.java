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
     * 未兑换
     */
    Integer NOT_REDEEMED = 1;
    /**
     * 已兑换
     */
    Integer REDEEMED = 2;


}
