package com.program.controller.common;

import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author linxf
 * @date 2024/8/17
 *
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/public/sign")
public class SignController {

    @Autowired
    private SignService signService;

    /**
     * 签到，可以补签
     * @return
     */
    @PostMapping("/signIn")
    public CommonResult signIn(@RequestBody  Map<String, Object> data,
                             HttpServletRequest request) {
        String date = (String) data.get("date");
        //统计连续签到的次数
        int count = signService.doSign(date,request);
        return CommonResult.build(count, CommonResultEnum.SIGN_SUCCESS);
    }

    /**
     * 获取签到次数 默认当月
     * @param date
     * @return
     */
    @GetMapping("/getSignCount")
    public CommonResult getSignCount(String date,HttpServletRequest request) {
        Long count = signService.getSignCount(date,request);
        return CommonResult.build(count, CommonResultEnum.SUCCESS_QUERY);
    }

    /**
     * 获取签到信息
     * @param dateStr
     * @param request
     * @return
     */
    @GetMapping("/getSignInfo")
    public CommonResult getSignInfo(String dateStr,HttpServletRequest request) {
        Map<String, Boolean> map = signService.getSignInfo(dateStr,request);
        return CommonResult.build(map, CommonResultEnum.SUCCESS_QUERY);
    }

}
