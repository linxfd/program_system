package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("系统菜单实体类")
@TableName(value = "sys_menu")
public class SysMenu extends CommonEntity{

    @ApiModelProperty(value = "父节点id")
    private Integer parentId;

    @ApiModelProperty(value = "节点标题")
    private String topMenuName;

    @ApiModelProperty(value = "组件名称")
    private String url;

    @ApiModelProperty(value = "图标")
    private String topIcon;

    @ApiModelProperty(value = "排序值")
    private Integer sortValue;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    private Integer status;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点")
    private List<SysMenu> children;

}
