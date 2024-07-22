package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.*;
import com.program.model.entity.User;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;
import com.program.model.vo.UserVo;

import java.util.List;


public interface UserService extends IService<User> {

    String register(RegisterDto registerDto);

    Boolean checkUsername(String username);

    String login(LoginDto loginDto);

    User getUserByUsername(String username);

    // 这里要reset cache 所以必须要有更新后的数据返回
    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    PageResponse<UserInfoVo> getUser(UserDto userDto);

    void handlerUser(Integer type, String userIds);

    void addUser(AddUserDto addUserDto);

    UserInfoVo getUserInfoById(Integer userId);

    List<UserInfoVo> getUserInfoByIds(List<Integer> userIds);

    // 根据id删除用户
    void deleteUserById(Integer id);

    //
    Boolean editUsername(User user);
}
