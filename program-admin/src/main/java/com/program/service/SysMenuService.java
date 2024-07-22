package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.SysMenu;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 */

public interface SysMenuService extends IService<SysMenu> {


    void saveSysMenu(SysMenu sysMenu);

    //查询所有菜单
    List<SysMenu> findNodes();


    //根据角色id查询菜单
    Map<String, Object> findSysRoleMenuByRoleId(Integer roleId);
}
