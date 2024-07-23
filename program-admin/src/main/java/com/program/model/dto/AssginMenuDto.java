package com.program.model.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
//"请求参数实体类"
public class AssginMenuDto {

    //@Schema(description = "角色id")
    private Long id;			// 角色id

    // 选中的菜单id的集合; Map的
    //          键id表示菜单的id，
    //          键isHalf表示是否为半开; 0否，1是
    //@Schema(description = "选中的菜单id的集合")
    private List<Map<String , Number>> menuIdList;

}