package com.program.controller.teacher.course;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.model.dto.SysMenuDto;
import com.program.model.entity.CourseCategory;
import com.program.model.entity.QuestionBank;
import com.program.model.entity.SysMenu;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CommonResultEnum;
import com.program.service.CourseCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 课程分类
 * @author lxf
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/teacher/category")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;


    @GetMapping("/findNodes")
    public CommonResult findNodes(){
        List<CourseCategory> list = courseCategoryService.findNodes();
        CommonResult build = CommonResult.build(list, CommonResultEnum.SUCCESS_QUERY);
        return build ;
    }

    @ApiOperation("添加课程分类")
    @PostMapping("/save")
    public CommonResult save(@RequestBody CourseCategory courseCategory) {
        courseCategoryService.saveCourseCategory(courseCategory);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_ADD);
    }

    @ApiOperation("修改课程分类")
    @PutMapping("/updateById")
    public CommonResult updateById(@RequestBody CourseCategory courseCategory) {
        courseCategoryService.updateById(courseCategory);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_UPDATE) ;
    }

    @ApiOperation("删除课程分类")
    @DeleteMapping("/removeById/{id}")
    @Transactional
    public CommonResult removeById(@PathVariable Integer id) {
        QueryWrapper<CourseCategory> qw = new QueryWrapper<CourseCategory>()
                .eq("id", id);
        CourseCategory courseCategory = new CourseCategory();
        courseCategory.setIsDeleted(1);
        courseCategoryService.update(courseCategory, qw);
        return CommonResult.build(null , CommonResultEnum.SUCCESS_QUERY) ;
    }


//    @GetMapping(value = "/detail/{id}")
//    public CommonResult detail(@PathVariable("id") String id){
//        return AjaxResult.success(courseCategoryService.getById(id));
//    }
//
//    @Log(title = "课程分类", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    public AjaxResult add(@RequestBody CourseCategory courseCategory){
//        return toAjax(courseCategoryService.save(courseCategory));
//    }
//
//
//    @Log(title = "课程分类", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    public AjaxResult edit(@RequestBody CourseCategory courseCategory){
//        return toAjax(courseCategoryService.updateById(courseCategory));
//    }
//
//
//    @Log(title = "课程分类", businessType = BusinessType.DELETE)
//    @GetMapping("/remove/{ids}")
//    public AjaxResult remove(@PathVariable String[] ids){
//        return toAjax(courseCategoryService.removeByIds(Arrays.asList(ids)));
//    }
}
