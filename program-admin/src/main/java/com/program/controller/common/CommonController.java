package com.program.controller.common;

import com.program.model.dto.*;
import com.program.model.entity.User;
import com.program.model.entity.Website;
import com.program.model.entity.WebsiteClassification;
import com.program.model.vo.*;
import com.program.service.UserRoleService;
import com.program.service.UserService;
import com.program.service.WebsiteClassificationService;
import com.program.service.WebsiteService;
import com.program.service.impl.UserRoleServiceImpl;
import com.program.service.impl.UserServiceImpl;
import com.program.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.program.model.vo.UserVo.fromUser;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/common")
@Api(tags = "(学生,教师,管理员)通用相关接口")
public class CommonController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private WebsiteClassificationService websiteClassificationService;

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
        Boolean aBoolean = userService.editUsername(user);
        return CommonResult.<Boolean>builder()
                .data(aBoolean)
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
        String login = userService.login(loginDto);
        return CommonResult.<String>builder()
                .data(login)
                .build();
    }

    @PostMapping("/phoneLogin")
    @ApiOperation("手机登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "系统用户唯一用户名", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "系统用户密码", required = true, dataType = "string", paramType = "body"),
    })
    public CommonResult<String> phoneLogin(@RequestBody @Valid PhoneLoginDto phoneLogin) {
        return CommonResult.<String>builder()
                .data(userService.phoneLogin(phoneLogin))
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
        User userByUsername = userService.getUserByUsername(JwtUtils.getUserInfoByToken(request).getUsername());
        return CommonResult.<UserVo>builder()
                .data(fromUser(userByUsername))
                .build();
    }

    @PostMapping("/updateCurrentUser")
    @ApiOperation("供给用户更改个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "系统用户实体", required = true, dataType = "user", paramType = "body")
    })
    public CommonResult updateCurrentUser(@RequestBody UpdateUserInfoDto updateUserInfoDto) {

        return userService.updateUserInfo(updateUserInfoDto);
    }


    @PostMapping("/updateCurrentPhone")
    @ApiOperation("供给用户换绑手机号")
    public CommonResult updateCurrentPhone(@RequestBody @Valid UpdatePhoneInfoDto updatePhoneInfoDto) {

        return userService.updateCurrentPhone(updatePhoneInfoDto);
    }

    /**
     * 获得网站列表--用户浏览列表
     * @return
     */
    @ApiOperation(value = "获得网站列表")
    @PostMapping("/website/list/{classificationId}")
    public CommonResult getListByclassificationId(@PathVariable("classificationId") Integer classificationId){
        // -1 代表全部
        if(classificationId == -1){
            classificationId = null;
        }
        List<Website> list = websiteService.pageUserList(classificationId);
        return CommonResult.<List<Website>>builder()
                .data(list)
                .build();
    }

    @ApiOperation(value = "获得网站分类列表列表")
    @PostMapping("/website/ClassificationList")
    public CommonResult getClassificationList(@RequestBody WebsiteClassificationVo WebsiteClassificationVo){
        PageResponse<WebsiteClassification> list = websiteClassificationService.pageList(WebsiteClassificationVo);
        return CommonResult.<PageResponse<WebsiteClassification>>builder()
                .data(list)
                .build();
    }
}
