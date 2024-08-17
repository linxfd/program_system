package com.program.model.vo;

import com.program.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class UserVo {
    private Integer id;

    private String username;

    private String password;

    private String trueName;

    private String phone;

    private String roleName;

    private Date createTime;

    //注册到今天的天数
    private String todate;

    public static UserVo fromUser(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

}
