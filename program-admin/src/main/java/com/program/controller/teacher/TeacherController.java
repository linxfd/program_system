package com.program.controller.teacher;

import com.program.model.dict.DictFileType;
import com.program.model.dict.DictStatus;
import com.program.model.dto.QuestionDto;
import com.program.model.dto.StudentExamRecordExcelDto;
import com.program.model.entity.ExamRecord;
import com.program.model.entity.MediaFiles;
import com.program.model.entity.Question;
import com.program.model.entity.QuestionBank;
import com.program.model.vo.*;
import com.program.service.*;
import com.program.utils.EmptyUtil;
import com.program.utils.HashUtil;
import com.program.utils.JwtUtils;
import com.program.utils.MinioUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "老师权限相关的接口")
@RequestMapping(value = "/teacher")
public class TeacherController {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MediaFilesService mediaFilesService;

    private final ExamService examService;

    private final UserService userService;

    private final QuestionService questionService;

    private final ExamRecordService examRecordService;

    private final QuestionBankService questionBankService;

    @GetMapping("/getQuestionBank")
    @ApiOperation("获取所有题库信息")
    public CommonResult<List<QuestionBank>> getQuestionBank(@RequestParam(required = false) String createPerson) {
        return CommonResult.<List<QuestionBank>>builder()
                .data(questionBankService.getAllQuestionBanks(createPerson))
                .build();
    }

    @GetMapping("/getQuestion")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionType", value = "问题类型", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "questionBank", value = "问题所属题库", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "questionContent", value = "问题内容", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "页面数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation("获取题目信息,可分页 ----> 查询条件(可无)(questionType,questionBank,questionContent),必须有的(pageNo,pageSize)")
    public CommonResult<PageResponse<Question>> getQuestion(@RequestParam(required = false) String questionType,
                                                            @RequestParam(required = false) String questionBank,
                                                            @RequestParam(required = false) String questionContent,
                                                            @RequestParam(required = false) String createPerson,
                                                            Integer pageNo, Integer pageSize) {
        return CommonResult.<PageResponse<Question>>builder()
                .data(questionService.getQuestion(questionType, questionBank, questionContent, createPerson,pageNo, pageSize))
                .build();
    }

    @GetMapping("/deleteQuestion")
    @ApiOperation("根据id批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionIds", value = "问题id的字符串以逗号分隔", required = true, dataType = "string", paramType = "query")
    })
    public CommonResult<Void> deleteQuestion(String questionIds) {
        questionService.deleteQuestionByIds(questionIds);
        return CommonResult.<Void>builder()
                .build();
    }

    @GetMapping("/addBankQuestion")
    @ApiOperation("将问题加入题库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionIds", value = "问题id的字符串以逗号分隔", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "banks", value = "题库id的字符串以逗号分隔", required = true, dataType = "string", paramType = "query")
    })
    public CommonResult<String> addBankQuestion(String questionIds, String banks) {
        questionBankService.addQuestionToBank(questionIds, banks);
        return CommonResult.<String>builder()
                .build();
    }

    @GetMapping("/removeBankQuestion")
    @ApiOperation("将问题从题库移除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionIds", value = "问题id的字符串以逗号分隔", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "banks", value = "题库id的字符串以逗号分隔", required = true, dataType = "string", paramType = "query")
    })
    public CommonResult<Void> removeBankQuestion(String questionIds, String banks) {
        questionBankService.removeBankQuestion(questionIds, banks);
        return CommonResult.<Void>builder()
                .build();
    }

    @PostMapping("/uploadImage")
    @ApiOperation("接受前端上传的图片,返回上传图片地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "file", paramType = "body")
    })
    public CommonResult<String> uploadImage(MultipartFile file,HttpServletRequest request) throws Exception {
        log.info("开始上传文件: {}", file.getOriginalFilename());
        // 生成MD5值
        String fileId = HashUtil.computeMD5(file.getInputStream());
        // 检查数据库中是否已有该文件ID
        MediaFiles mediaFiles = mediaFilesService.getById(fileId);
        String url = "";

        if (EmptyUtil.isEmpty(mediaFiles)) {
            url = minioUtil.fileUpload(file, DictFileType.IMAGE_TYPE);

            MediaFiles newMediaFile = new MediaFiles();
            newMediaFile.setId(fileId);
            newMediaFile.setFileName(file.getOriginalFilename());
            newMediaFile.setFileType(DictFileType.IMAGE);
            newMediaFile.setUrl(url);
            newMediaFile.setFileSize(file.getSize());

            // 获取当前登录用户信息
            TokenVo userInfoByToken = JwtUtils.getUserInfoByToken(request);
            // 设置创建人
            newMediaFile.setCreatePerson(userInfoByToken.getUsername());

            newMediaFile.setStatus(DictStatus.NORMAL); // 正常状态
            String description = request.getHeader("description");

            newMediaFile.setRemark(description); // 备注
            newMediaFile.setCreateTime(new Date());
            newMediaFile.setUpdateTime(new Date());

            // 保存到数据库
            mediaFilesService.save(newMediaFile);
        }else {
            url = mediaFiles.getUrl();
        }
        return CommonResult.<String>builder()
                .data(url)
                .message("上传成功")
                .build();
    }


    @PostMapping("/uploadVideo")
    @ApiOperation("接受前端上传的视频,返回上传图片地址")
    public CommonResult<String> uploadVideo(MultipartFile file,HttpServletRequest request) throws Exception {
        log.info("开始上传文件: {}", file.getOriginalFilename());
        // 生成MD5值
        String fileId = HashUtil.computeMD5(file.getInputStream());
        // 检查数据库中是否已有该文件ID
        MediaFiles mediaFiles = mediaFilesService.getById(fileId);
        String url = "";

        if (EmptyUtil.isEmpty(mediaFiles)) {
            url = minioUtil.fileUpload(file, DictFileType.VIDEO_TYPE);

            MediaFiles newMediaFile = new MediaFiles();
            newMediaFile.setId(fileId);
            newMediaFile.setFileName(file.getOriginalFilename());
            newMediaFile.setFileType(DictFileType.VIDEO);
            newMediaFile.setUrl(url);
            newMediaFile.setFileSize(file.getSize());

            // 获取当前登录用户信息
            TokenVo userInfoByToken = JwtUtils.getUserInfoByToken(request);
            // 设置创建人
            newMediaFile.setCreatePerson(userInfoByToken.getUsername());

            newMediaFile.setStatus(DictStatus.NORMAL); // 正常状态
            String description = request.getHeader("description");

            newMediaFile.setRemark(description); // 备注
            newMediaFile.setCreateTime(new Date());
            newMediaFile.setUpdateTime(new Date());

            // 保存到数据库
            mediaFilesService.save(newMediaFile);
        }else {
            url = mediaFiles.getUrl();
        }
        return CommonResult.<String>builder()
                .data(url)
                .message("上传成功")
                .build();
    }

    @PostMapping("/addQuestion")
    @ApiOperation("添加试题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionVo", value = "问题的vo视图对象", required = true, dataType = "questionVo", paramType = "body")
    })
    public CommonResult<Void> addQuestion(@RequestBody @Valid QuestionVo questionVo) {
        questionService.addQuestion(questionVo);
        return CommonResult.<Void>builder().build();
    }

    @PostMapping("/updateQuestion")
    @ApiOperation("更新试题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionVo", value = "问题的vo视图对象", required = true, dataType = "questionVo", paramType = "body")
    })
    public CommonResult<Void> updateQuestion(@RequestBody @Valid QuestionVo questionVo) {
        questionService.updateQuestion(questionVo);
        return CommonResult.<Void>builder()
                .build();
    }

    @GetMapping("/deleteQuestionBank")
    @ApiOperation("删除题库并去除所有题目中的包含此题库的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除题库的id字符串逗号分隔", required = true, dataType = "string", paramType = "query")
    })
    public CommonResult<Void> deleteQuestionBank(String ids) {
        questionBankService.deleteQuestionBank(ids);
        return CommonResult.<Void>builder()
                .build();
    }

    @PostMapping("/addQuestionBank")
    @ApiOperation("添加题库信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionBank", value = "题库的实体对象", required = true, dataType = "questionBank", paramType = "body")
    })
    public CommonResult<Void> addQuestionBank(@RequestBody QuestionBank questionBank, HttpServletRequest request) {
        questionBankService.addQuestionBank(questionBank,request);
        return CommonResult.<Void>builder()
                .build();
    }

    @GetMapping("/operationExam/{type}")
    @ApiOperation("操作考试的信息表(type 1启用 2禁用 3删除)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "操作类型", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "ids", value = "操作的考试id集合", required = true, dataType = "string", paramType = "query")
    })
    public CommonResult<Void> operationExam(@PathVariable("type") Integer type, String ids) {
        examService.operationExam(type, ids);
        return CommonResult.<Void>builder()
                .build();
    }

    @PostMapping("/addExamByBank")
    @ApiOperation("根据题库添加考试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addExamByBankVo", value = "根据题库添加考试vo对象", required = true, dataType = "addExamByBankVo", paramType = "body")
    })
    public CommonResult<Void> addExamByBank(@RequestBody @Valid AddExamByBankVo addExamByBankVo, HttpServletRequest request) {
        examService.addExamByBank(addExamByBankVo,request);
        return CommonResult.<Void>builder()
                .build();
    }

    @PostMapping("/addExamByQuestionList")
    @ApiOperation("根据题目列表添加考试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addExamByQuestionVo", value = "通过题目列表添加考试的vo对象", required = true, dataType = "addExamByQuestionVo", paramType = "body")
    })
    public CommonResult<Void> addExamByQuestionList(@RequestBody @Valid AddExamByQuestionVo addExamByQuestionVo, HttpServletRequest request) {
        examService.addExamByQuestionList(addExamByQuestionVo,request);
        return CommonResult.<Void>builder()
                .build();
    }

    @PostMapping("/updateExamInfo")
    @ApiOperation("更新考试的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addExamByQuestionVo", value = "通过题目列表添加考试的vo对象", required = true, dataType = "addExamByQuestionVo", paramType = "body")
    })
    public CommonResult<Void> updateExamInfo(@RequestBody AddExamByQuestionVo addExamByQuestionVo) {
        examService.updateExamInfo(addExamByQuestionVo);
        return CommonResult.<Void>builder()
                .message("更新成功")
                .build();
    }

    @GetMapping("/getExamRecord")
    @ApiOperation("获取考试记录信息,(pageNo,pageSize)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examId", value = "考试id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "页面数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<PageResponse<ExamRecord>> getExamRecord(@RequestParam(required = false) Integer examId,
                                                                @RequestParam(required = false) String createPerson,
                                                                Integer pageNo, Integer pageSize) {
        return CommonResult.<PageResponse<ExamRecord>>builder()
                .data(examRecordService.getExamRecord(examId,createPerson, pageNo, pageSize))
                .build();
    }

    @GetMapping("/getUserById/{userId}")
    @ApiOperation("根据用户id查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<UserInfoVo> getUserById(@PathVariable Integer userId) {
        return CommonResult.<UserInfoVo>builder()
                .data(userService.getUserInfoById(userId))
                .build();
    }

    @GetMapping("/getUserByIds")
    @ApiOperation("根据用户ids查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "用户ids", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<List<UserInfoVo>> getUserByIds(@RequestParam("userIds") List<Integer> userIds) {
        return CommonResult.<List<UserInfoVo>>builder()
                .data(userService.getUserInfoByIds(userIds))
                .build();
    }

    @GetMapping("/setObjectQuestionScore")
    @ApiOperation("设置考试记录的客观题得分,设置总分为逻辑得分+客观题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "totalScore", value = "总成绩", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "examRecordId", value = "考试记录id", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<Void> setObjectQuestionScore(Integer totalScore, Integer examRecordId) {
        examRecordService.setObjectQuestionScore(totalScore, examRecordId);
        return CommonResult.<Void>builder()
                .build();
    }

    @GetMapping("/getExamPassRate")
    @ApiOperation("提供每一门考试的通过率数据(echarts绘图)")
    public CommonResult<List<String>> getExamPassRate(@RequestParam(required = false) String createPerson) {
        return CommonResult.<List<String>>builder()
                .data(examService.getExamPassRateEchartData(createPerson))
                .build();
    }

    @GetMapping("/getExamNumbers")
    @ApiOperation("提供每一门考试的考试次数(echarts绘图)")
    public CommonResult<List<String>> getExamNumbers(@RequestParam(required = false) String createPerson) {
        return CommonResult.<List<String>>builder()
                .data(examService.getExamNumbersEchartData(createPerson))
                .build();
    }

    @GetMapping("/exportStudentExamRecordToExcel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "response", value = "response", required = true, dataType = "HttpServletResponse", paramType = "spring"),
            @ApiImplicitParam(name = "examId", value = "考试id", required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation("根据考试将已阅卷的所有学生成绩导出至Excel")
    public CommonResult<List<StudentExamRecordExcelDto>> exportStudentExamRecordToExcel(HttpServletResponse response, @RequestParam(name = "examId") Integer examId) throws IOException {
        List<StudentExamRecordExcelDto> allStudentScoreByExamId = examService.getAllStudentScoreByExamId(examId);
        return CommonResult.<List<StudentExamRecordExcelDto>>builder()
                .data(allStudentScoreByExamId)
                .build();
    }
    @PostMapping("/importQurstion")
    @ApiOperation("导入题目")
    public CommonResult importQurstion(QuestionDto questionDto, MultipartFile file) {
        questionService.importQurstion(questionDto,file);
        return CommonResult.<Void>builder()
                .message("更新成功")
                .build();
    }
    @GetMapping("/getQuestionExportHand")
    @ApiOperation("导出题目")
    public CommonResult<PageResponse<QuestionAnswerVo>> getQuestionExportHand(@RequestParam(required = false) String questionType,
                                              @RequestParam(required = false) String questionBank,
                                              @RequestParam(required = false) String questionContent,
                                              @RequestParam(required = false) String createPerson,
                                              Integer pageNo, Integer pageSize) {
        PageResponse<QuestionAnswerVo> questionAnswerVos = questionService.getQuestionExportHand(questionType,questionBank,questionContent,createPerson,pageNo,pageSize);
        return CommonResult.<PageResponse<QuestionAnswerVo>>builder()
                .data(questionAnswerVos)
                .build();
    }
}
