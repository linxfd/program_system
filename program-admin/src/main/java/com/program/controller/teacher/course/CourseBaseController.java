package com.program.controller.teacher.course;

import java.util.Arrays;
import java.util.List;

import com.program.model.dto.CourseBaseDto;
import com.program.model.dto.CourseDto;
import com.program.model.dto.UserDto;
import com.program.model.entity.CourseBase;
import com.program.model.entity.CourseCategory;
import com.program.model.entity.CourseUnit;
import com.program.model.vo.*;
import com.program.service.CourseBaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 课程基本信息
 * @author lxf
 * @date 2024-09-05
 */
@RestController
@RequestMapping("/teacher/coursebase")
public class CourseBaseController {
    @Autowired
    private CourseBaseService courseBaseService;


    @PostMapping("/list")
    public CommonResult<PageResponse<CourseBase>> list(@RequestBody CourseBaseDto courseBase){
        return CommonResult.<PageResponse<CourseBase>>builder()
                .data(courseBaseService.list(courseBase))
                .build();
    }

    @PostMapping("/addCategory")
    @ApiOperation("添加课程")
    public CommonResult addCategory(@RequestBody CourseDto courseDto, HttpServletRequest request){
        courseBaseService.addCategory(courseDto,request);
        return CommonResult.<Boolean>builder()
                .data(null)
                .build();
    }

    @ApiOperation("查询审计")
    @GetMapping("/queryAudit/{id}")
    public CommonResult queryAudit(@PathVariable Integer id){
        List<CourseUnitVo> courseUnitVo = courseBaseService.queryAudit(id);
        return CommonResult.<List<CourseUnitVo>>builder()
                .data(courseUnitVo)
                .message(CommonResultEnum.SUCCESS_QUERY.getMessage())
                .build();
    }

    @ApiOperation("查询需要编辑的课程信息")
    @GetMapping("/getCategoryInfo/{id}")
    public CommonResult getCategoryInfo(@PathVariable Integer id){
        CourseDto courseUnitVo = courseBaseService.getCategoryInfo(id);
        return CommonResult.<CourseDto>builder()
                .data(courseUnitVo)
                .message(CommonResultEnum.SUCCESS_QUERY.getMessage())
                .build();
    }

    @PostMapping("/updateCourse")
    @ApiOperation("修改课程")
    public CommonResult updateCourse(@RequestBody CourseDto courseDto, HttpServletRequest request){
        courseBaseService.updateCourse(courseDto,request);
        return CommonResult.<Boolean>builder()
                .data(null)
                .build();
    }


    @ApiOperation("删除课程信息")
    @GetMapping("/deleteCourse/{id}")
    public CommonResult deleteCourse(@PathVariable Integer id){
        courseBaseService.deleteCourse(id);
        return CommonResult.<Void>builder()
                .data(null)
                .message(CommonResultEnum.SUCCESS_DELETE.getMessage())
                .build();
    }

    @GetMapping("/handle/{type}")
    @ApiOperation("type=1(启用) 2(禁用) 3(删除) ids(需要操作的id)")
    public CommonResult<Void> handle(@PathVariable("type") Integer type, String ids) {
        courseBaseService.handle(type, ids);
        return CommonResult.<Void>builder().build();
    }
    
//    @PreAuthorize("@ss.hasPermi('work:base:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(CourseBase courseBase){
//        startPage();
//        List<CourseBase> list = courseBaseService.selectList(courseBase);
//        return getDataTable(list);
//    }
//
//    @PreAuthorize("@ss.hasPermi('work:base:query')")
//    @GetMapping(value = "/detail/{id}")
//    public AjaxResult detail(@PathVariable("id") Long id){
//        return AjaxResult.success(courseBaseService.getById(id));
//    }
//
//    @PreAuthorize("@ss.hasPermi('work:base:add')")
//    @Log(title = "课程基本信息", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    public AjaxResult add(@RequestBody CourseBase courseBase){
//        return toAjax(courseBaseService.save(courseBase));
//    }
//
//    @PreAuthorize("@ss.hasPermi('work:base:edit')")
//    @Log(title = "课程基本信息", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    public AjaxResult edit(@RequestBody CourseBase courseBase){
//        return toAjax(courseBaseService.updateById(courseBase));
//    }
//
//    @PreAuthorize("@ss.hasPermi('work:base:remove')")
//    @Log(title = "课程基本信息", businessType = BusinessType.DELETE)
//    @GetMapping("/remove/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids){
//        return toAjax(courseBaseService.removeByIds(Arrays.asList(ids)));
//    }
}
