package com.program.controller.generalAdmin;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linxf
 * @date 2024/9/9
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/generalAdmin")
@Api(tags = "普通管理员相关接口")
public class generalAdminController {


}
