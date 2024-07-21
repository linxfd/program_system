package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 */
//"系统菜单实体类"
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("系统公告实体")
@TableName(value = "sys_menu")
public class SysMenu {

    //"父节点id"

    private Integer parentId;

    //@Schema(description = "节点标题")
    private String topMenuName;

    //@Schema(description = "组件名称")
    private String url;

    //@Schema(description = "图标")
    private String topIcon;

    //@Schema(description = "排序值")
    private Integer sortValue;

    //@Schema(description = "状态(0:禁止,1:正常)")
    private Integer status;

    // 下级列表
    @TableField(exist = false)
    //@Schema(description = "子节点")
    private List<SysMenu> children;

    @TableId(type = IdType.AUTO)
    // @Schema(description = "唯一标识")
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Schema(description = "修改时间")
    private Date updateTime;

    //@Schema(description = "是否删除")
    private Integer isDeleted;
}
