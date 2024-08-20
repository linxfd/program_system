package com.program.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.SysMenuMapper;
import com.program.mapper.SysRoleMenuMapper;
import com.program.model.dict.IsDeleted;
import com.program.model.entity.SysMenu;
import com.program.model.entity.SysRoleMenu;
import com.program.model.entity.User;
import com.program.service.SysMenuService;
import com.program.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper ;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    @Transactional
    public void saveSysMenu(SysMenu sysMenu) {

        // 添加新的节点
        sysMenuMapper.insert(sysMenu);
        // 新添加一个菜单，那么此时就需要将该菜单所对应的父级菜单设置为半开
        updateSysRoleMenuIsHalf(sysMenu) ;
    }

    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<SysMenu>();
        wrapper.eq("is_deleted", IsDeleted.NOT_DELETED);
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return null;
        }
        //构建树形数据
        List<SysMenu> treeList = MenuHelper.buildTree(sysMenuList);
        return treeList;
    }

    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Integer roleId) {
        // 查询所有的菜单数据
        List<SysMenu> sysMenuList = this.findNodes() ;

        // 查询当前角色的菜单数据
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId) ;

        // 将数据存储到Map中进行返回
        Map<String , Object> result = new HashMap<>() ;
        result.put("sysMenuList" , sysMenuList) ;
        result.put("roleMenuIds" , roleMenuIds) ;

        // 返回
        return result;
    }


    // 递归调用,将菜单所对应的父级菜单设置为半开
    private void updateSysRoleMenuIsHalf(SysMenu sysMenu) {
        // 查询是否存在父节点
        QueryWrapper<SysMenu> wr = new QueryWrapper<SysMenu>();
        wr.eq("is_deleted", IsDeleted.NOT_DELETED);
        wr.eq("id", sysMenu.getParentId());
        SysMenu parentMenu = sysMenuMapper.selectOne(wr);
        if(parentMenu != null) {
            // 将该id的菜单设置为半开
            QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<SysRoleMenu>().eq("menu_id", parentMenu.getId()) ;
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setIsHalf(1);
            sysRoleMenuMapper.update(sysRoleMenu, wrapper) ;
            // 递归调用
            updateSysRoleMenuIsHalf(parentMenu) ;
        }
    }
}
