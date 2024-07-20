package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.entity.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<User> {
}
