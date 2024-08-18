package com.program.controller.common;

import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.model.vo.TokenVo;
import com.program.service.SignService;
import com.program.utils.DateMyUtil;
import com.program.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
        Map<String, Object> map = signService.doSign(date,request);
        return CommonResult.build(map, CommonResultEnum.SIGN_SUCCESS);
    }

    /**
     * 获取签到次数 默认当月
     * @return
     */
    @GetMapping("/getSignCount/{date}")
    public CommonResult getSignCount(@PathVariable String date,HttpServletRequest request) {
        Long count = signService.getSignCount(date,request);
        return CommonResult.build(count, CommonResultEnum.SUCCESS_QUERY);
    }

    /**
     * 获取签到信息
     * @param dateStr
     * @param request
     * @return
     */
    @GetMapping("/getSignInfo/{dateStr}")
    public CommonResult getSignInfo(@PathVariable String dateStr,HttpServletRequest request) {
        Map<String, Boolean> map = signService.getSignInfo(dateStr,request);
        return CommonResult.build(map, CommonResultEnum.SUCCESS_QUERY);
    }

    @GetMapping("/getContinuousSignCount/{date}")
    public CommonResult getContinuousSignCount (@PathVariable String date,HttpServletRequest request) {
        TokenVo userInfo  = JwtUtils.getUserInfoByToken(request);
        Date data = DateMyUtil.getDate(date);
        int count  = signService.getContinuousSignCount(userInfo.getId(),data);
        return CommonResult.build(count, CommonResultEnum.SUCCESS_QUERY);
    }

}
