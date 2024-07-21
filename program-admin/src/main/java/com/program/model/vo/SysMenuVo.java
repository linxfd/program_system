package com.program.model.vo;

import com.program.model.entity.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 */
@Data
public class SysMenuVo  {
    //@Schema(description = "节点标题")
    private String topMenuName;

    //@Schema(description = "组件名称")
    private String url;

    //@Schema(description = "图标")
    private String topIcon;

    //@Schema(description = "子节点")
    private List<SysMenuVo> children;
}