package com.program.controller.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.model.dict.DictPoints;
import com.program.model.dict.DictStatus;
import com.program.model.dto.WebsiteDto;
import com.program.model.entity.*;
import com.program.service.*;
import com.program.model.vo.*;
import com.program.utils.EmptyUtil;
import com.program.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "公共的相关接口")
@RequestMapping(value = "/public")
public class PublicController {

    private final NoticeService noticeService;

    private final ExamService examService;

    private final QuestionBankService questionBankService;

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private PointsOrderService pointsOrderService;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private UserService userService;

    @PostMapping("/getExamInfo")
    @ApiOperation("根据信息查询考试的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examQueryVo", value = "考试信息查询vo对象", required = true, dataType = "examQueryVo", paramType = "body")
    })
    public CommonResult<PageResponse<Exam>> getExamInfo(@RequestBody ExamQueryVo examQueryVo) {
        return CommonResult.<PageResponse<Exam>>builder()
                .data(examService.getExamPage(examQueryVo))
                .build();
    }

    @GetMapping("/getExamInfoById")
    @ApiOperation("根据考试id查询考试的信息和题目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examId", value = "考试id", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<AddExamByQuestionVo> getExamInfoById(@RequestParam Integer examId) {
        return CommonResult.<AddExamByQuestionVo>builder()
                .data(examService.getExamInfoById(examId))
                .build();
    }

    @GetMapping("/allExamInfo")
    @ApiOperation("查询考试所有信息")
    public CommonResult<List<Exam>> allExamInfo() {
        return CommonResult.<List<Exam>>builder()
                .data(examService.list(null))
                .build();
    }

    @GetMapping("/getBankHaveQuestionSumByType")
    @ApiOperation("获取题库中所有题目类型的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankName", value = "题库名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "页面数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true, dataType = "int", paramType = "query"),
    })
    public CommonResult<PageResponse<BankHaveQuestionSum>> getBankHaveQuestionSumByType(@RequestParam(required = false) String bankName,
                                                                                        @RequestParam(required = false)String createPerson,
                                                                                        Integer pageNo, Integer pageSize) {
        return CommonResult.<PageResponse<BankHaveQuestionSum>>builder()
                .data(questionBankService.getBankHaveQuestionSumByType(bankName, createPerson,pageNo, pageSize))
                .build();
    }

    @GetMapping("/getQuestionByBankIdAndType")
    @ApiOperation("根据题库id和题目类型获取题目信息 type(1单选 2多选 3判断)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "题库id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "题目类型", required = true, dataType = "int", paramType = "query"),
    })
    public CommonResult<List<QuestionVo>> getQuestionByBankIdAndType(Integer bankId, Integer type) {
        return CommonResult.<List<QuestionVo>>builder()
                .data(questionBankService.getQuestionByBankIdAndType(bankId, type))
                .build();
    }

    @GetMapping("/getQuestionByBank")
    @ApiOperation("根据题库获取所有的题目信息(单选,多选,判断题)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "题库id", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<List<QuestionVo>> getQuestionByBank(Integer bankId) {
        return CommonResult.<List<QuestionVo>>builder()
                .data(questionBankService.getQuestionsByBankId(bankId))
                .build();
    }

    @GetMapping("/getCurrentNewNotice")
    @ApiOperation("获取当前系统最新的公告")
    public CommonResult<String> getCurrentNewNotice() {
        return CommonResult.<String>builder()
                .data(noticeService.getCurrentNotice())
                .build();
    }
    @GetMapping("/getCreatePersonName")
    @ApiOperation("获取所有角色是老师的用户")
    public CommonResult getCreatePersonName() {
        return CommonResult.<List<User>>builder()
                .data(userService.getCreatePersonName())
                .build();
    }

    @ApiOperation(value = " 获取兑换状态")
    @GetMapping("/getRedemptionStatus/{type}")
    public CommonResult getRedemptionStatus(@PathVariable("type") Integer type, Integer itemId,HttpServletRequest request){
        QueryWrapper<PointsOrder> orderQueryWrapper = new QueryWrapper<>();
        User user = userService.getById(JwtUtils.getUserInfoByToken(request).getId());
        orderQueryWrapper.eq("item_id", itemId);
        orderQueryWrapper.eq("order_type", type);
        orderQueryWrapper.eq("user_id", user.getId());
        PointsOrder one = pointsOrderService.getOne(orderQueryWrapper);
        PointsVo pointsVo = new PointsVo();

        if(EmptyUtil.isEmpty(one)){
            CourseBase courseBase = courseBaseService.getById(itemId);
            //是否兑换
            pointsVo.setIsRedeemed(DictStatus.NOT_REDEEMED);
            //需要的积分
            pointsVo.setPointsNumber(courseBase.getPointsNumber());
            //当前拥有积分
            pointsVo.setPointsTotal(user.getPoints());
        }else{
            pointsVo.setIsRedeemed(DictStatus.REDEEMED);
        }
        return CommonResult.<PointsVo>builder()
                .data(pointsVo)
                .build();
    }


    @ApiOperation(value = "兑换")
    @GetMapping("/redemption/{type}")
    @Transactional
    public CommonResult redemption(@PathVariable("type") Integer type, Integer itemId,HttpServletRequest request){
        Integer userId = JwtUtils.getUserInfoByToken(request).getId();
        User user = userService.getById(userId);
        //判断积分是否足够
        CourseBase courseBase = courseBaseService.getById(itemId);
        if(user.getPoints() < courseBase.getPointsNumber()){
            return CommonResult.<Boolean>builder()
                    .data(false)
                    .message("积分不足")
                    .build();
        }else{
            //积分扣减
            user.setPoints(user.getPoints() - courseBase.getPointsNumber());
            userService.updateById(user);

            //保存积分流水
            Points points = new Points();
            points.setUserId(userId);
            points.setUsername(user.getUsername());
            points.setObtainMethod(DictPoints.METHOD_REDEMPTION);
            points.setNotes("兑换课程："+courseBase.getName());
            //积分流水,取负数
            points.setPointsFlow(-courseBase.getPointsNumber());
            pointsService.save(points);

            //保存兑换记录
            PointsOrder pointsOrder = new PointsOrder();
            pointsOrder.setItemId(itemId);
            pointsOrder.setOrderType(type);
            pointsOrder.setUserId(userId);
            pointsOrder.setPoints(courseBase.getPointsNumber());
            pointsOrderService.save(pointsOrder);

            return CommonResult.<Boolean>builder()
                    .data(true)
                    .message("成功兑换"+courseBase.getName())
                    .build();
        }

    }

}
