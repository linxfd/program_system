package com.program.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linxf
 * @date 2024/7/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuDto {

    //@Schema(description = "角色id")
    private Integer roleId;

    //@Schema(description = "权限id")
    private Integer menuId;

}
