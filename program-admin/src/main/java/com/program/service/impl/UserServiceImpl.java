package com.program.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.model.dict.DictRole;
import com.program.model.dto.*;
import com.program.model.entity.User;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.mapper.UserMapper;
import com.program.model.vo.*;
import com.program.utils.*;
import com.program.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.program.utils.CommonUtils.setLikeWrapper;
import static com.program.model.vo.UserInfoVo.fromUser;


@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final RedisUtil redisUtil;

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
        user.setRoleId(DictRole.ROLE_STUDENT);
        user.setPhone(registerDto.getPhone());
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
    public String phoneLogin(PhoneLoginDto phoneLogin) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phoneLogin.getPhone()));
        // 判断是否已经发送验证码
        String code = (String)redisUtil.get("phone:code:" + phoneLogin.getPhone());

        if(phoneLogin.getCodePhone() == null || !phoneLogin.getCodePhone().equals(code)) {
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        // 用户不存在, 则自动注册
        if (user == null) {
            //生成随机用户名
            String name = RandomUtils.generateUsername();
            String password = RandomUtils.generateUsername();
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername(name);
            registerDto.setPassword(password);
            registerDto.setPhone(phoneLogin.getPhone());
            return register(registerDto);
        }

        // 用户存在判断状态是否正常的
        if (user.getStatus() == 1) {
            // 发放token令牌
            return JwtUtils.createToken(user);
        } else {
            // 密码错误 或者 账号封禁
            throw new BusinessException(CommonErrorCode.E_100101);
        }
    }

    @Override
    public CommonResult updateCurrentPhone(UpdatePhoneInfoDto updatePhoneInfoDto) {
        // 判断手机号是否被其他用户绑定
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", updatePhoneInfoDto.getPhone()));
        if(!EmptyUtil.isNotUtils(user)){
            throw new BusinessException(CommonErrorCode.E_100107);
        }
        // 判断验证码是否正确
        String phoneCode = (String)redisUtil.get("phone:code:" + updatePhoneInfoDto.getPhone());
        if(EmptyUtil.isNotUtils(updatePhoneInfoDto.getCodePhone())||
                !updatePhoneInfoDto.getCodePhone().equals(phoneCode)
        ){
            throw new BusinessException(CommonErrorCode.E_100104);
        }
        // 获取当前用户, 更新手机号
        User userNew = userMapper.selectOne(new QueryWrapper<User>().eq("username", updatePhoneInfoDto.getUsername()));
        userNew.setPhone(updatePhoneInfoDto.getPhone());
        userNew.setUpdateTime(new Date());

        return CommonResult.build(userMapper.updateById(userNew), CommonResultEnum.SUCCESS_UPDATE);
    }

    @Override
//    @Cache(prefix = "user", suffix = "#username", ttl = 10, randomTime = 2, timeUnit = TimeUnit.HOURS)
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public CommonResult updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        User OldUser = userMapper.selectOne(new QueryWrapper<User>().eq("id", updateUserInfoDto.getId()));
        String oldPassword = SaltEncryption.saltEncryption(updateUserInfoDto.getOldPassword(), OldUser.getSalt());

        // 根据旧密码修改密码
        if (!EmptyUtil.isNotUtils(updateUserInfoDto.getOldPassword())  && !OldUser.getPassword().equals(oldPassword)){
            throw new BusinessException(CommonErrorCode.E_100100);
        }

        // 根据验证码修改密码
        String phoneCode = (String)redisUtil.get("phone:code:" + updateUserInfoDto.getPhone());
        if(EmptyUtil.isNotUtils(updateUserInfoDto.getOldPassword())){
            String code = updateUserInfoDto.getCodePhone();
            // 当前密码为空时, 验证码不能为空
            if(EmptyUtil.isNotUtils(code)){
                throw new BusinessException(CommonErrorCode.E_100108);
            }
            if(!code.equals(phoneCode)){
                throw new BusinessException(CommonErrorCode.E_100104);
            }
        }
        if(!EmptyUtil.isNotUtils(updateUserInfoDto.getPassword()) ){
            String newPwd = SaltEncryption.saltEncryption(updateUserInfoDto.getPassword(), OldUser.getSalt());
            OldUser.setPassword(newPwd);
        }
        OldUser.setUsername(updateUserInfoDto.getUsername());
        OldUser.setTrueName(updateUserInfoDto.getTrueName());
        return CommonResult.build(userMapper.updateById(OldUser), CommonResultEnum.SUCCESS_UPDATE);
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
        if (!EmptyUtil.isNotUtils(userDto.getRoleId()) ){
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
        user.setRoleId(addUserDto.getRoleId());
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
        if (EmptyUtil.isNotUtils(user1.getUsername())) {
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
        User user1 = userMapper.selectOne(qw);
        if (!EmptyUtil.isNotUtils(user.getPassword())) {
            user1.setPassword(SaltEncryption.saltEncryption(user.getPassword(), user1.getSalt()));
        }
        user1.setPhone(user.getPhone());
        user1.setTrueName(user.getTrueName());
        user1.setUsername(user.getUsername());
        user1.setRoleId(user.getRoleId());
        user1.setUpdateTime(new Date());
        userMapper.update(user1, qw);
    }

    @Override
    public List<User>  getCreatePersonName() {
        List<User> createPersonName = userMapper.getCreatePersonName();
        return createPersonName;
    }



    public Boolean checkeditUserPhone(User user) {
        // 查询当前用户,如果当前用户名没有变化，则直接返回true
        User user1 = userMapper.selectOne(new QueryWrapper<User>().eq("id", user.getId()));
        // 如果当前用户手机号为空，则直接验证
        if (EmptyUtil.isNotUtils(user1.getPhone())) {
            return this.checkUserPhone(user.getPhone());
        }
        if (user1.getPhone().equals(user.getPhone())) {
            return true;
        }else {
            return this.checkUserPhone(user.getPhone());
        }
    }
}
