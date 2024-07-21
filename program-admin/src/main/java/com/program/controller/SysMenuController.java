package com.program.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.program.model.entity.SysMenu;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 */
@RestController
@RequestMapping(value="/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

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
    public CommonResult removeById(@PathVariable Integer id) {
        QueryWrapper<SysMenu> qw = new QueryWrapper<SysMenu>()
                .eq("id", id);
        SysMenu sysMenu = new SysMenu();
        sysMenu.setIsDeleted(1);
        sysMenuService.update(sysMenu, qw);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_QUERY) ;
    }
}
