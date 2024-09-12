package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@TableName(value = "points_order")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("积分明细表")
public class PointsOrder extends CommonEntity{

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "课程或题库的id")
    private Integer itemId;

    @ApiModelProperty(value = "积分数量")
    private Integer points;

    @ApiModelProperty(value = "订单类型，1表示课程，2表示题库")
    private Integer orderType;
}