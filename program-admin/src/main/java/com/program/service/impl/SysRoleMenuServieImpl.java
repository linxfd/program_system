package com.program.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.SysRoleMenuMapper;
import com.program.model.dto.SysMenuDto;
import com.program.model.entity.SysRoleMenu;
import com.program.service.SysRoleMenuServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author linxf
 * @date 2024/7/22
 */
@Service
public class SysRoleMenuServieImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuServie {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;


    @Override
    public void deleteBySysMenuDto(SysMenuDto sm) {
        sysRoleMenuMapper.deleteBySysMenuDto(sm);
    }
}
