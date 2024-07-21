package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色权限")
@TableName(value = "sys_role_menu")
public class SysRoleMenu {
    //@Schema(description = "角色id")
    private Integer roleId;
    //@Schema(description = "权限id")
    private Integer menuId;
    // @Schema(description = "唯一标识")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Schema(description = "修改时间")
    private Date updateTime;

    //@Schema(description = "是否删除")
    private Integer isDeleted;
    //@Schema(description = "是否半选")
    private Integer isHalf;
}
