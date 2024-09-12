package com.program.controller.common;

import com.program.model.dto.CourseBaseDto;
import com.program.model.entity.CourseBase;
import com.program.model.vo.*;
import com.program.service.CourseBaseService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author linxf
 * @date 2024/9/11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/public/course")
public class CourseController {

    @Autowired
    private CourseBaseService courseBaseService;


    @PostMapping("/list")
    @ApiOperation("查询课程的信息")
    public CommonResult<PageResponse<CourseBase>> list(@RequestBody CourseBaseDto courseBase){
        return CommonResult.<PageResponse<CourseBase>>builder()
                .data(courseBaseService.list(courseBase))
                .build();
    }

    @ApiOperation("查询课程信息和视频单元集合")
    @GetMapping("/getCourseInfo/{id}")
    public CommonResult getCourseInfo(@PathVariable Integer id){
        CourseInfoVO courseInfoVO = courseBaseService.getCourseInfo(id);
        return CommonResult.<CourseInfoVO>builder()
                .data(courseInfoVO)
                .message(CommonResultEnum.SUCCESS_QUERY.getMessage())
                .build();
    }


}
