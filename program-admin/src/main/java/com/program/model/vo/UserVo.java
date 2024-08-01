package com.program.model.vo;

import com.program.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserVo {

    private String username;

    private String password;

    private String trueName;

    private String phone;

    public static UserVo fromUser(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

}
