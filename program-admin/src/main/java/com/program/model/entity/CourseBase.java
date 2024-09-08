package com.program.model.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
/**
 * @author lxf
 * @date 2024-09-05
 */
@Data
@TableName(value = "course_base")
@Accessors(chain = true)
public class CourseBase extends CommonEntity{

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "创建人")
    private String createPerson;

    @ApiModelProperty(value = "大分类")
    private String mt;

    @ApiModelProperty(value = "小分类")
    private String st;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片")
    private String pic;

    @ApiModelProperty(value = "课程发布状态 1:未发布  2:已发布")
    private Long courseStatus;


    @ApiModelProperty(value = "是否需要积分，1不需要，2需要")
    private Long charge;

    @ApiModelProperty(value = "积分数量")
    private Long pointsNumber;

}
