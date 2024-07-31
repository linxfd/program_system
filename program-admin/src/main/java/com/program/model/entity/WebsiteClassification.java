package com.program.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linxf
 * @date 2024/7/31
 */
@Data
public class WebsiteClassification extends CommonEntity{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "网站分类名称")
    private String name;

    @ApiModelProperty(value = "网站分类注解")
    private String notes;

}
