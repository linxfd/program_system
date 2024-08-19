package com.program.controller.common;

import com.program.model.dto.PointsDto;
import com.program.model.entity.Points;
import com.program.model.entity.Website;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.model.vo.PageResponse;
import com.program.model.vo.TokenVo;
import com.program.service.PointsService;
import com.program.service.SignService;
import com.program.utils.DateMyUtil;
import com.program.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private PointsService pointsService;

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

    /**
     * 获得初始数据
     * 累计签到积分、连续的签到次数
     * @param date
     * @param request
     * @return
     */
    @GetMapping("/getSignCountInfo/{date}")
    public CommonResult getSignCountInfo(@PathVariable String date,HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        TokenVo userInfo  = JwtUtils.getUserInfoByToken(request);
        Date data = DateMyUtil.getDate(date);

        //统计连续签到的次数
        int continuousSignCount  = signService.getContinuousSignCount(userInfo.getId(),data);

        //获取用户累计签到积分
        int accumulatedSignCount  = pointsService.getAccumulatedSignCount(userInfo.getId());

        map.put("continuousSignCount",continuousSignCount);
        map.put("accumulatedSignCount",accumulatedSignCount);
        return CommonResult.build(map, CommonResultEnum.SUCCESS_QUERY);
    }


    @PostMapping("/list")
    public CommonResult list(@RequestBody PointsDto pointsDto){
        PageResponse<Points> list = pointsService.pageList(pointsDto);
        return CommonResult.<PageResponse<Points>>builder()
                .data(list)
                .build();
    }

    @GetMapping(value = "/detail/{id}")
    public CommonResult detail(@PathVariable("id") Integer id){
        return CommonResult.build(pointsService.getById(id), CommonResultEnum.SUCCESS_QUERY);
    }



    @PostMapping("/add")
    public CommonResult add(@RequestBody Points points){
        return CommonResult.build(pointsService.save(points), CommonResultEnum.SUCCESS_ADD);
    }



    @PostMapping("/edit")
    public CommonResult edit(@RequestBody Points points){
        return CommonResult.build(pointsService.updateById(points), CommonResultEnum.SUCCESS_UPDATE);
    }



    @GetMapping("/remove/{ids}")
    public CommonResult remove(@PathVariable Integer[] ids){
        return CommonResult.build(pointsService.removeByIds(Arrays.asList(ids)),
                CommonResultEnum.SUCCESS_DELETE);
    }

}
