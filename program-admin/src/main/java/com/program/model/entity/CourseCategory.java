package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lxf
 * @date 2024-09-04
 */
@Data
@TableName(value = "course_category")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("课程分类")
public class CourseCategory extends CommonEntity{

    @ApiModelProperty(value = "父结点id:第=一级的父节点是0，自关联字段id")
    private Integer parentId;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类标签默认和名称一样")
    private String label;

    @ApiModelProperty(value = "排序值")
    private Integer sortValue;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    private Integer status;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点")
    private List<CourseCategory> children;
}
