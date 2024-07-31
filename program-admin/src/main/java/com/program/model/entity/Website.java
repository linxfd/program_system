package com.program.model.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
/**
 * @author lxf
 * @date 2024-07-31
 */
@Data
@TableName(value = "website")
@Accessors(chain = true)
public class Website extends CommonEntity{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "网址")
    private String url;

    @ApiModelProperty(value = "网站图标")
    private String icon;

    @ApiModelProperty(value = "网站注解")
    private String notes;

    @ApiModelProperty(value = "网站名称")
    private String name;

    @ApiModelProperty(value = "网站分类")
    private String classified;
}
