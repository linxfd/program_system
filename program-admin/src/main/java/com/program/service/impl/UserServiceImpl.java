package com.program.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.annotation.Cache;
import com.program.model.dto.*;
import com.program.model.entity.User;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.mapper.UserMapper;
import com.program.utils.NotUtils;
import com.program.model.vo.UserVo;
import com.program.service.UserService;
import com.program.utils.JwtUtils;
import com.program.utils.SaltEncryption;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.program.utils.CommonUtils.setLikeWrapper;
import static com.program.model.vo.UserInfoVo.fromUser;


@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public String register(RegisterDto registerDto) {
        if (!checkUsername(registerDto.getUsername())) {
            throw new BusinessException(CommonErrorCode.E_100103);
        }
        // 盐值
        String salt = UUID.randomUUID().toString().substring(0, 6);
        String newPwd = SaltEncryption.saltEncryption(registerDto.getPassword(), salt);

        User user = new User();
        BeanUtils.copyProperties(registerDto, user);
        user.setPassword(newPwd);
        user.setSalt(salt);
        user.setRoleId(1);
        user.setCreateTime(new Date());

        userMapper.insert(user);
        // 发放token令牌
        return JwtUtils.createToken(user);
    }

    @Override
    public Boolean checkUsername(String username) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("username", username)) < 1;
    }

    @Override
    public String login(LoginDto loginDto) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        if (user == null) {
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        String saltPassword = SaltEncryption.saltEncryption(loginDto.getPassword(), user.getSalt());
        // 对用户输入的密码加密后 对比数据库的密码 并且用户的状态是正常的
        if (saltPassword.equals(user.getPassword()) && user.getStatus() == 1) {
            // 发放token令牌
            return JwtUtils.createToken(user);
        } else {
            // 密码错误 或者 账号封禁
            throw new BusinessException(CommonErrorCode.E_100101);
        }
    }

    @Override
    @Cache(prefix = "user", suffix = "#username", ttl = 10, randomTime = 2, timeUnit = TimeUnit.HOURS)
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    @Cache(prefix = "user", suffix = "#updateUserInfoDto.getUsername()", ttl = 10, randomTime = 2, timeUnit = TimeUnit.HOURS, resetCache = true)
    public User updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        User user = getUserByUsername(updateUserInfoDto.getUsername());
        user.updateFrom(updateUserInfoDto);
        userMapper.updateById(user);
        return user;
    }

    @Override
    public PageResponse<UserInfoVo> getUser(UserDto userDto) {
        IPage<User> userPage = new Page<>(userDto.getPageNo(), userDto.getPageSize());
        // 查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("username", userDto.getUsername());
        queryParams.put("true_name", userDto.getTrueName());

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        setLikeWrapper(wrapper, queryParams);
        // 当角色id不为null时查询
        if (!NotUtils.isNotUtils(userDto.getRoleId()) ){
            wrapper.eq("role_id",userDto.getRoleId());
        }
        wrapper.eq("is_deleted", 0);

        wrapper.orderByAsc("role_id","status","create_time");

        userPage = userMapper.selectPage(userPage, wrapper);
        List<UserInfoVo> records = userPage.getRecords().stream().map(user -> UserInfoVo.fromUser(user)).collect(Collectors.toList());

        return PageResponse.<UserInfoVo>builder().data(records).total(userPage.getTotal()).build();
    }

    @Override
    public void handlerUser(Integer type, String userIds) {
        // 转换成数组 需要操作的用户的id数组
        String[] ids = userIds.split(",");
        switch (type) {
            case 1:
                updateUserStatus(ids, 1);
                break;
            case 2:
                updateUserStatus(ids, 2);
                break;
            case 3:
                for (String id : ids) {
                    userMapper.deleteById(Integer.parseInt(id));
                }
                break;
            default:
                throw new BusinessException(CommonErrorCode.E_100105);
        }
    }

    @Override
    public void addUser(AddUserDto addUserDto) {
        // 盐值
        String salt = UUID.randomUUID().toString().substring(0, 6);
        String newPwd = SaltEncryption.saltEncryption(addUserDto.getPassword(), salt);
        User user = addUserDto.toUser();
        user.setPassword(newPwd);
        user.setSalt(salt);
        user.setPhone(addUserDto.getPhone());
        user.setCreateTime(new Date());
        userMapper.insert(user);
    }

    @Override
    public UserInfoVo getUserInfoById(Integer userId) {
        return fromUser(userMapper.selectById(userId));
    }

    @Override
    public List<UserInfoVo> getUserInfoByIds(List<Integer> userIds) {
        return userMapper.selectBatchIds(userIds).stream()
                .map(UserInfoVo::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Integer id) {
        QueryWrapper<User> qw = new QueryWrapper<User>().eq("id", id);
        User user = new User();
        user.setIsDeleted(1);
        userMapper.update(user, qw);
    }

    @Override
    public Boolean editUsername(User user) {
        // 查询当前用户,如果当前用户名没有变化，则直接返回true
        // 如果当前用户名有变化，则判断当前用户名是否被占用
        User user1 = userMapper.selectOne(new QueryWrapper<User>().eq("id", user.getId()));
        if (NotUtils.isNotUtils(user1.getUsername())) {
            return this.checkUsername(user.getUsername());
        }
        if (user1.getUsername().equals(user.getUsername())) {
            return true;
        }else {
            return this.checkUsername(user.getUsername());
        }
    }

    private void updateUserStatus(String[] ids, Integer status) {
        for (String id : ids) {
            // 当前需要修改的用户
            User user = userMapper.selectById(Integer.parseInt(id));
            user.setStatus(status);// 设置为启用的用户
            userMapper.updateById(user);
        }
    }

    public Boolean checkUserPhone(String phone) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("phone", phone)) < 1;
    }

    public void updateUser(User user) {
        QueryWrapper<User> qw = new QueryWrapper<User>().eq("id", user.getId());
        if (!NotUtils.isNotUtils(user.getPassword())) {
            user.setPassword(SaltEncryption.saltEncryption(user.getPassword(), user.getSalt()));
        }
        user.setUpdateTime(new Date());
        userMapper.update(user, qw);
    }

    public Boolean checkeditUserPhone(User user) {
        // 查询当前用户,如果当前用户名没有变化，则直接返回true
        User user1 = userMapper.selectOne(new QueryWrapper<User>().eq("id", user.getId()));
        // 如果当前用户手机号为空，则直接验证
        if (NotUtils.isNotUtils(user1.getPhone())) {
            return this.checkUserPhone(user.getPhone());
        }
        if (user1.getPhone().equals(user.getPhone())) {
            return true;
        }else {
            return this.checkUserPhone(user.getPhone());
        }
    }
}
