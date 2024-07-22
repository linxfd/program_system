package com.program.controller;

import com.program.model.entity.UserRole;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
