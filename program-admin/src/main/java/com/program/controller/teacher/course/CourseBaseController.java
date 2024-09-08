package com.program.controller.teacher.course;

import java.util.Arrays;
import java.util.List;

import com.program.model.dto.CourseBaseDto;
import com.program.model.dto.UserDto;
import com.program.model.entity.CourseBase;
import com.program.model.entity.CourseCategory;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;
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
