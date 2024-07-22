package com.program.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.annotation.Cache;
import com.program.mapper.SysMenuMapper;
import com.program.model.dto.AssginMenuDto;
import com.program.model.entity.SysMenu;
import com.program.model.entity.UserRole;
import com.program.mapper.UserRoleMapper;
import com.program.model.vo.SysMenuVo;
import com.program.service.SysMenuService;
import com.program.service.UserRoleService;
import com.program.utils.MenuHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;



    @Override
//    @Cache(prefix = "menu", suffix = "#roleId", ttl = 60 * 60 * 24, randomTime = 60 * 60 * 10)
    public String getMenuInfo(Integer roleId) {
        List<SysMenu> sysMenuList = sysMenuMapper.getAllSysMenu(roleId);
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenus);
        String sysMenuVos1 = JSONObject.toJSONString(sysMenuVos);
        UserRole role = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("role_id", roleId));
        return sysMenuVos1;
    }

    @Override
//    @Cache(prefix = "userRoles", ttl = 30, randomTime = 10, timeUnit = TimeUnit.DAYS)
    public List<UserRole> getUserRole() {
        List<UserRole> userRoles = userRoleMapper.selectList(null);
        return userRoles;
    }

    @Override
    @Transactional
    public void doAssign(AssginMenuDto assginMenuDto) {
        // 根据角色的id删除其所对应的菜单数据
        userRoleMapper.deleteByRoleId(assginMenuDto.getRoleId());

        // 获取菜单的id
        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();
        if(menuInfo != null && menuInfo.size() > 0) {
            // 保存菜单
            userRoleMapper.doAssign(assginMenuDto) ;
        }
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTopMenuName(sysMenu.getTopMenuName());
            sysMenuVo.setUrl(sysMenu.getUrl());
            sysMenuVo.setTopIcon(sysMenu.getTopIcon());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
