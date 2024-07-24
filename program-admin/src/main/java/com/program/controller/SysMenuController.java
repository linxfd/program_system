package com.program.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.program.mapper.SysRoleMenuMapper;
import com.program.model.dto.SysMenuDto;
import com.program.model.entity.SysMenu;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 */
@RestController
@RequestMapping(value="/admin/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 查询菜单列表
     * @return
     */
    //@Operation(summary  = "菜单列表")
    @GetMapping("/findNodes")
    public CommonResult<List<SysMenu>> findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        CommonResult build = CommonResult.build(list, CommonResultEnum.SUCCESS_QUERY);
        return build ;
    }

    //添加菜单
    @PostMapping("/save")
    public CommonResult save(@RequestBody SysMenu sysMenu) {
        sysMenuService.saveSysMenu(sysMenu);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_ADD);
    }

    //@Operation(summary = "修改菜单")
    @PutMapping("/updateById")
    public CommonResult updateById(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_UPDATE) ;
    }
    //@Operation(summary = "删除菜单")
    @DeleteMapping("/removeById/{id}")
    @Transactional
    public CommonResult removeById(@PathVariable Integer id) {
        QueryWrapper<SysMenu> qw = new QueryWrapper<SysMenu>()
                .eq("id", id);
        SysMenu sysMenu = new SysMenu();
        sysMenu.setIsDeleted(1);
        sysMenuService.update(sysMenu, qw);
        //删除角色菜单关联
        SysMenuDto sysMenuDto = new SysMenuDto();
        sysMenuDto.setMenuId(id);
        sysRoleMenuMapper.deleteBySysMenuDto(sysMenuDto);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_QUERY) ;
    }
    @GetMapping("/findNodes/{id}")
    public CommonResult GetSysRoleMenuIds(@PathVariable(value = "id") Integer id) {
        Map<String , Object> sysRoleMenuList = sysMenuService.findSysRoleMenuByRoleId(id) ;
        return CommonResult.build(sysRoleMenuList , CommonResultEnum.SUCCESS_QUERY) ;
    }
}
