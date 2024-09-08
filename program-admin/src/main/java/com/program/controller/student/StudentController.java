package com.program.controller.student;

import com.program.model.dict.DictFileType;
import com.program.model.entity.ExamQuestion;
import com.program.model.entity.ExamRecord;
import com.program.service.ExamQuestionService;
import com.program.service.QuestionService;
import com.program.service.impl.ExamRecordServiceImpl;
import com.program.utils.MinioUtil;
import com.program.utils.OSSUtil;
import com.program.model.vo.CommonResult;
import com.program.model.vo.PageResponse;
import com.program.model.vo.QuestionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "学生权限相关的接口")
@RequestMapping(value = "/student")
public class StudentController {

    private final OSSUtil ossUtil;

    @Autowired
    private MinioUtil minioUtil;

    private final ExamRecordServiceImpl examRecordService;

    private final QuestionService questionService;

    private final ExamQuestionService examQuestionService;

    @GetMapping("/getMyGrade")
    @ApiOperation("获取个人成绩(分页 根据考试名查询)")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "系统唯一用户名", required = true, dataType = "string", paramType = "query"), @ApiImplicitParam(name = "pageNo", value = "当前页面数", required = true, dataType = "int", paramType = "query"), @ApiImplicitParam(name = "pageSize", value = "当前页面大小", required = true, dataType = "int", paramType = "query"), @ApiImplicitParam(name = "examId", value = "考试唯一id", dataType = "int", paramType = "query")})
    public CommonResult<PageResponse<ExamRecord>> getMyGrade(String username, Integer pageNo, Integer pageSize, @RequestParam(required = false) Integer examId) {
        return CommonResult.<PageResponse<ExamRecord>>builder().data(examRecordService.getUserGrade(username, examId, pageNo, pageSize)).build();
    }

    @GetMapping("/getCertificate")
    @ApiOperation("生成证书接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "examName", value = "考试名称", required = true, dataType = "string", paramType = "query"), @ApiImplicitParam(name = "examRecordId", value = "考试记录id", required = true, dataType = "int", paramType = "query")})
    public void getCertificate(HttpServletResponse response, @RequestParam(name = "examName") String examName, @RequestParam(name = "examRecordId") Integer examRecordId) throws UnsupportedEncodingException {
        examRecordService.createExamCertificate(response, URLDecoder.decode(examName, "UTF-8"), examRecordId);
    }

    @PostMapping("/addExamRecord")
    @ApiOperation("保存考试记录信息,返回保存记录的id")
    @ApiImplicitParams({@ApiImplicitParam(name = "examRecord", value = "考试记录实体对象", required = true, dataType = "examRecord", paramType = "body")})
    public CommonResult<Integer> addExamRecord(@RequestBody ExamRecord examRecord, HttpServletRequest request) {
        return CommonResult.<Integer>builder().data(examRecordService.addExamRecord(examRecord, request)).build();
    }

    @GetMapping("/getQuestionById/{id}")
    @ApiOperation("根据id获取题目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "问题id", required = true, dataType = "int", paramType = "path")
    })
    public CommonResult<QuestionVo> getQuestionById(@PathVariable("id") Integer id) {
        QuestionVo questionVoById = questionService.getQuestionVoById(id);
        return CommonResult.<QuestionVo>builder()
                .data(questionVoById)
                .build();
    }

    @GetMapping("/getQuestionByIds")
    @ApiOperation("根据id集合获取题目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "问题id集合", required = true, dataType = "int", paramType = "params")
    })
    public CommonResult<PageResponse<QuestionVo>> getQuestionById(@RequestParam("ids") List<Integer> ids) {
        return CommonResult.<PageResponse<QuestionVo>>builder()
                .data(questionService.getQuestionVoByIds(ids))
                .build();
    }

    @GetMapping("/getExamRecordById/{recordId}")
    @ApiOperation("根据考试的记录id查询用户考试的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordId", value = "考试记录id", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<ExamRecord> getExamRecordById(@PathVariable Integer recordId) {
        return CommonResult.<ExamRecord>builder()
                .data(examRecordService.getExamRecordById(recordId))
                .build();
    }

    @GetMapping("/getExamQuestionByExamId/{examId}")
    @ApiOperation("根据考试id查询考试中的每一道题目id和分值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "examId", value = "考试id", required = true, dataType = "int", paramType = "query")
    })
    public CommonResult<ExamQuestion> getExamQuestionByExamId(@PathVariable Integer examId) {
        return CommonResult.<ExamQuestion>builder()
                .data(examQuestionService.getExamQuestionByExamId(examId))
                .build();
    }

    @PostMapping("/uploadQuestionImage")
    @ApiOperation("接受前端上传的图片,返回上传图片地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "file", paramType = "body")
    })
    public CommonResult<String> uploadQuestionImage(MultipartFile file) throws Exception {
        log.info("开始上传文件: {}", file.getOriginalFilename());
        return CommonResult.<String>builder()
                .data(minioUtil.fileUpload(file, DictFileType.IMAGE_TYPE))
                .message("上传成功")
                .build();
    }
}
