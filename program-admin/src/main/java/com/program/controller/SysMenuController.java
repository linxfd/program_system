package com.program.controller;

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
        return CommonResult.build(list , CommonResultEnum.SUCCESS) ;
    }

    //添加菜单
    @PostMapping("/save")
    public CommonResult save(@RequestBody SysMenu sysMenu) {
        sysMenuService.saveSysMenu(sysMenu);
        return CommonResult.build(null , CommonResultEnum.SUCCESS);
    }

    //@Operation(summary = "修改菜单")
    @PutMapping("/updateById")
    public CommonResult updateById(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return CommonResult.build(null , CommonResultEnum.SUCCESS) ;
    }
    //@Operation(summary = "删除菜单")
    @DeleteMapping("/removeById/{id}")
    public CommonResult removeById(@PathVariable Long id) {
        sysMenuService.removeById(id);
        return CommonResult.build(null , CommonResultEnum.SUCCESS) ;
    }
}
