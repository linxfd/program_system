package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.entity.SysRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 */
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    // 根据角色id查询菜单id
    List<Long> findSysRoleMenuByRoleId(Integer roleId);
}
