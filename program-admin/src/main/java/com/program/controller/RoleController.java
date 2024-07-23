package com.program.controller;

import com.program.model.dto.AssginMenuDto;
import com.program.model.entity.UserRole;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/role")
@Api(tags = "角色相关接口")
public class RoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/getRole")
    @ApiOperation("查询系统存在的所有角色信息")
    public CommonResult<List<UserRole>> getRole() {
        return CommonResult.build(userRoleService.getUserRole(), CommonResultEnum.SUCCESS_OBTAIN);
    }

    @ApiOperation("根据角色id分配菜单权限")
    @PostMapping("/doAssign")
    public CommonResult doAssign(@RequestBody AssginMenuDto assginMenuDto) {
        userRoleService.doAssign(assginMenuDto);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_UPDATE) ;
    }

    @ApiOperation("添加角色")
    @PostMapping("/addRole")
    public CommonResult addRole(@RequestBody UserRole userRole) {
        userRoleService.addRole(userRole);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_UPDATE) ;
    }
    @ApiOperation("修改角色")
    @PostMapping("/updateRole")
    public CommonResult updateRole(@RequestBody UserRole userRole) {
        userRoleService.updateRole(userRole);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_UPDATE) ;
    }

    @ApiOperation("删除角色")
    @GetMapping("/deleteRole/{id}")
    public CommonResult deleteRole(@PathVariable Integer id) {

        return userRoleService.deleteRole(id);
    }
}
