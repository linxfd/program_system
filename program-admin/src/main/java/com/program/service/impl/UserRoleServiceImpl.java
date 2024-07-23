package com.program.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.annotation.Cache;
import com.program.mapper.SysMenuMapper;
import com.program.mapper.SysRoleMenuMapper;
import com.program.mapper.UserMapper;
import com.program.model.dto.AssginMenuDto;
import com.program.model.dto.SysMenuDto;
import com.program.model.entity.SysMenu;
import com.program.model.entity.User;
import com.program.model.entity.UserRole;
import com.program.mapper.UserRoleMapper;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.model.vo.SysMenuVo;
import com.program.service.SysMenuService;
import com.program.service.SysRoleMenuServie;
import com.program.service.UserRoleService;
import com.program.utils.MenuHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuServie sysRoleMenuServie;

    @Autowired
    private UserMapper userMapper;

    @Override
//    @Cache(prefix = "menu", suffix = "#roleId", ttl = 60 * 60 * 24, randomTime = 60 * 60 * 10)
    public String getMenuInfo(Integer id) {
        List<SysMenu> sysMenuList = sysMenuMapper.getAllSysMenu(id);
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenus);
        String sysMenuVos1 = JSONObject.toJSONString(sysMenuVos);
        UserRole role = userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("id", id));
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
        userRoleMapper.deleteByRoleId(assginMenuDto.getId());

        // 获取菜单的id
        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();
        if(menuInfo != null && menuInfo.size() > 0) {
            // 保存菜单
            userRoleMapper.doAssign(assginMenuDto) ;
        }
    }

    @Override
    public void addRole(UserRole userRole) {
        userRole.setIsDeleted(0);
        userRole.setCreateTime(new Date());
        userRole.setUpdateTime(new Date());
        // 保存角色
        userRoleMapper.insert(userRole);
    }

    @Override
    public void updateRole(UserRole userRole) {
        userRole.setUpdateTime(new Date());
        userRoleMapper.updateById(userRole);
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

    @Override
    @Transactional
    public CommonResult deleteRole(Integer id) {

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("role_id", id));
        if (user != null) {
            return CommonResult.build(null, CommonResultEnum.FAIL_DELETE);
        }
        // 根据角色id删除角色菜单关联
        SysMenuDto sm = new SysMenuDto();
        sm.setRoleId(id);
        sysRoleMenuServie.deleteBySysMenuDto(sm);
        // 删除角色
        userRoleMapper.deleteById(id);

    return CommonResult.build(null, CommonResultEnum.SUCCESS_DELETE);
    }
}
