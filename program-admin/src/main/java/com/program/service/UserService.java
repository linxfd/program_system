package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.AddUserDto;
import com.program.model.dto.LoginDto;
import com.program.model.dto.RegisterDto;
import com.program.model.dto.UpdateUserInfoDto;
import com.program.model.entity.User;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;

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
