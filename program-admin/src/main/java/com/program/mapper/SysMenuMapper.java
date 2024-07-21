package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.entity.SysMenu;

import java.util.List;

/**
 * @version 1.0
 */
public interface SysMenuMapper  extends BaseMapper<SysMenu> {
    List<SysMenu> getAllSysMenu(Integer roleId);

    // 查询所有菜单
    List<SysMenu> selectList();



}
