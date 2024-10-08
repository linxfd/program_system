package com.program.model.vo;

import com.program.model.entity.CommonEntity;
import com.program.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo extends CommonEntity {

    private Integer id;

    private String username;

    private String trueName;

    private Integer roleId;

    private Integer status;

    private String phone;


    public static UserInfoVo fromUser(User user) {
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return userInfoVo;
    }
}
