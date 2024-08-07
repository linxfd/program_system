package com.program.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 异常响应
 */
@Data
@AllArgsConstructor
public class RestErrorResponse {

    private String errCode;

    private String errMsg;

}
