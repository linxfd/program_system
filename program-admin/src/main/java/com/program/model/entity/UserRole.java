package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色实体")
@TableName(value = "user_role")
public class UserRole extends CommonEntity  {

    //  对应数据库的主键(uuid,自增id,雪花算法, redis,zookeeper)
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "角色id", example = "1(学生) 2(教师) 3(管理员)")
    private Integer roleId;

    @ApiModelProperty(value = "用户角色名称", example = "1(学生) 2(教师) 3(管理员)")
    private String roleName;

    @ApiModelProperty(value = "角色信息", example = "学生主要服务对象")
    private String roleInfo;
}
