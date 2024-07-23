package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.SysMenuDto;
import com.program.model.entity.SysRoleMenu;

/**
 * @author linxf
 * @date 2024/7/22
 */
public interface SysRoleMenuServie extends IService<SysRoleMenu> {

    // 根据菜单id或角色id删除角色菜单关联
    void deleteBySysMenuDto(SysMenuDto sm);
}
