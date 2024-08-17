package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.*;
import com.program.model.entity.User;
import com.program.model.vo.CommonResult;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;
import com.program.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService extends IService<User> {

    String register(RegisterDto registerDto);

    Boolean checkUsername(String username);

    String login(LoginDto loginDto);

    User getUserByUsername(String username);

    // 这里要reset cache 所以必须要有更新后的数据返回
    CommonResult updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    PageResponse<UserInfoVo> getUser(UserDto userDto);

    void handlerUser(Integer type, String userIds);

    void addUser(AddUserDto addUserDto);

    UserInfoVo getUserInfoById(Integer userId);

    List<UserInfoVo> getUserInfoByIds(List<Integer> userIds);

    // 根据id删除用户
    void deleteUserById(Integer id);

    //
    Boolean editUsername(User user);

    // 检查手机号
    Boolean checkUserPhone(String phone);

    // 修改用户手机号
    Boolean checkeditUserPhone(User user);

    // 管理员修改用户
    void updateUser(User user);

    // 获取所有角色是老师的用户
    List<User> getCreatePersonName();

    // 手机号登录
    String phoneLogin(PhoneLoginDto phoneLogin);

    //换绑手机号
    CommonResult updateCurrentPhone(UpdatePhoneInfoDto updatePhoneInfoDto);

}
