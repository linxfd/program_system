package com.program.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserInfoDto {

    @NotBlank
    private String username;

    private String password;

    private String oldPassword;

    private String trueName;

    private String phone;
}
