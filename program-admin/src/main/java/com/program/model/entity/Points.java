package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linxf
 * @date 2024/8/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("积分明细表")
@TableName(value = "points")
public class Points extends CommonEntity{

    @ApiModelProperty(value = "用户id", example = "12")
    private Integer userId;

    @ApiModelProperty(value = "登录用户名", example = "wzz")
    private String username;

    @ApiModelProperty(value = "积分流水", example = "100")
    private Integer pointsFlow;

    @ApiModelProperty(value = "积分备注", example = "奖励")
    private String notes;

    @ApiModelProperty(value = "获取积分类型", example = "0其他，1签到，2考试")
    private Integer obtainMethod;

}
