package com.program.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author linxf
 * @date 2024/8/1
 */
@Data
public class UpdatePhoneInfoDto {
    @NotBlank
    private String username;

    private String phone;

    private String codePhone;
}
