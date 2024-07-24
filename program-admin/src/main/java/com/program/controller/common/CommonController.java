package com.program.controller.common;

import com.program.model.dto.LoginDto;
import com.program.model.dto.RegisterDto;
import com.program.model.dto.UpdateUserInfoDto;
import com.program.model.entity.User;
import com.program.service.UserRoleService;
import com.program.service.UserService;
import com.program.service.impl.UserRoleServiceImpl;
import com.program.service.impl.UserServiceImpl;
import com.program.utils.JwtUtils;
import com.program.model.vo.CommonResult;
import com.program.model.vo.TokenVo;
import com.program.model.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.program.model.vo.UserVo.fromUser;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/common")
@Api(tags = "(学生,教师,管理员)通用相关接口")
public class CommonController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    @RequestMapping("/error")
    public CommonResult<String> error() {
        return CommonResult.<String>builder()
                .code(233)
                .message("请求错误!")
                .build();
    }

    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "系统用户实体", required = true, dataType = "user", paramType = "body")
    })
    public CommonResult<String> Register(@RequestBody @Valid RegisterDto registerDto) {
        return CommonResult.<String>builder()
                .data(userService.register(registerDto))
                .build();
    }

    @ApiOperation("用户名合法查询接口")
    @GetMapping("/check/{username}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "系统用户唯一用户名", required = true, dataType = "string", paramType = "path")
    })
    public CommonResult<Boolean> checkUsername(@PathVariable(value = "username") String username) {
        return CommonResult.<Boolean>builder()
                .data(userService.checkUsername(username))
                .build();
    }

    @PostMapping ("/editUsername")
    @ApiOperation("修改用户时查询用户是否唯一")
    public CommonResult<Boolean> editUsername(@RequestBody User user){
        return CommonResult.<Boolean>builder()
                .data(userService.editUsername(user))
                .build();
    }

    @ApiOperation("手机号唯一绑定")
    @GetMapping("/checkPhone/{phone}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "系统用户唯一手机", required = true, dataType = "string", paramType = "path")
    })
    public CommonResult<Boolean> checkUserPhone(@PathVariable(value = "phone") String phone) {
        return CommonResult.<Boolean>builder()
                .data(userService.checkUserPhone(phone))
                .build();
    }
    @ApiOperation("修改时手机号唯一绑定")
    @PostMapping("/checkeditUserPhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "系统用户唯一手机", required = true, dataType = "string", paramType = "path")
    })
    public CommonResult<Boolean> checkeditUserPhone(@RequestBody User user) {
        return CommonResult.<Boolean>builder()
                .data(userService.checkeditUserPhone(user))
                .build();
    }

    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "系统用户唯一用户名", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "系统用户密码", required = true, dataType = "string", paramType = "body"),
    })
    public CommonResult<String> login(@RequestBody @Valid LoginDto loginDto) {
        return CommonResult.<String>builder()
                .data(userService.login(loginDto))
                .build();
    }

    @GetMapping("/getMenu")
    @ApiOperation(value = "获取不同用户的权限菜单")
    public CommonResult<String> getMenu(HttpServletRequest request) {

        String menuInfo = userRoleService.getMenuInfo(JwtUtils.getUserInfoByToken(request).getRoleId());
        CommonResult<String> stringCommonResult = new CommonResult<>();
        stringCommonResult.setData(menuInfo);
        return stringCommonResult;
    }

    @GetMapping("/checkToken")
    @ApiOperation("验证用户token接口")
    public CommonResult<TokenVo> checkToken(HttpServletRequest request) {
        return CommonResult.<TokenVo>builder()
                .data(JwtUtils.getUserInfoByToken(request))
                .build();
    }

    @GetMapping("/getCurrentUser")
    @ApiOperation("供给普通用户查询个人信息使用")
    public CommonResult<UserVo> getCurrentUser(HttpServletRequest request) {
        return CommonResult.<UserVo>builder()
                .data(fromUser(userService.getUserByUsername(JwtUtils.getUserInfoByToken(request).getUsername())))
                .build();
    }

    @PostMapping("/updateCurrentUser")
    @ApiOperation("供给用户更改个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "系统用户实体", required = true, dataType = "user", paramType = "body")
    })
    public CommonResult<Object> updateCurrentUser(@RequestBody @Valid UpdateUserInfoDto updateUserInfoDto) {
        userService.updateUserInfo(updateUserInfoDto);
        return CommonResult.builder()
                .build();
    }

}
