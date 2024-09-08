package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author linxf
 * @date 2024/9/8
 */

@Data
@TableName(value = "course_unit")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("课程视频单元")
public class CourseUnit extends CommonEntity{

    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "课程视频地址")
    private String url;

    @ApiModelProperty(value = "排序")
    private Integer sortValue;

    @ApiModelProperty(value = "视频审核状态，1:未审核 2:不通过 3通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "课程状态 1启用 2禁用", example = "1")
    private Integer status;

}
