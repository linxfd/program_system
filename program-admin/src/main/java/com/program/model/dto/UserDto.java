package com.program.model.dto;

import com.program.model.entity.User;
import lombok.Data;

/**
 * @version 1.0
 */
@Data
public class UserDto extends User {

    // 当前页码
    private Integer pageNo;
    // 每页显示条数
    private Integer pageSize;

}
