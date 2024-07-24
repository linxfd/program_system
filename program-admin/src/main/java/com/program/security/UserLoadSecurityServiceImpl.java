package com.program.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.model.entity.User;
import com.program.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

/**
 *  加载用户特定数据的核心接口。里面定义了一个根据用户名查询用户信息的方法。
 */
@Service
@RequiredArgsConstructor
public class UserLoadSecurityServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapperByUsername = new QueryWrapper<User>().eq("username", username);
        User user = Optional.of(userMapper.selectOne(queryWrapperByUsername))
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRoleId() + ""));

        return withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(roles)
                .build();
    }
}
