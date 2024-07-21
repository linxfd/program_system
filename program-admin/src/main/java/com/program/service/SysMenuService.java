package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.SysMenu;

import java.util.List;

/**
 * @version 1.0
 */

public interface SysMenuService extends IService<SysMenu> {


    void saveSysMenu(SysMenu sysMenu);

    //查询所有菜单
    List<SysMenu> findNodes();

}
