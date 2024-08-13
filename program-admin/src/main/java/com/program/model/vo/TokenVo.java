package com.program.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVo {

    private Integer roleId;

    private String username;

    private String trueName;

    private String password;

}
