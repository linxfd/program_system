package com.program.model.dto;

import com.program.model.entity.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AddUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Integer roleId;

    private String trueName;

    private String phone;

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

}
