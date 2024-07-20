package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.dto.AddUserDto;
import com.program.dto.LoginDto;
import com.program.dto.RegisterDto;
import com.program.dto.UpdateUserInfoDto;
import com.program.entity.User;
import com.program.vo.PageResponse;
import com.program.vo.UserInfoVo;

import java.util.List;


public interface UserService extends IService<User> {

    String register(RegisterDto registerDto);

    Boolean checkUsername(String username);

    String login(LoginDto loginDto);

    User getUserByUsername(String username);

    // 这里要reset cache 所以必须要有更新后的数据返回
    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    PageResponse<UserInfoVo> getUser(String loginName, String trueName, Integer pageNo, Integer pageSize);

    void handlerUser(Integer type, String userIds);

    void addUser(AddUserDto addUserDto);

    UserInfoVo getUserInfoById(Integer userId);

    List<UserInfoVo> getUserInfoByIds(List<Integer> userIds);
}
