package com.program.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.CourseCategory;

/**
 * @author lxf
 * @date 2024-09-04
 */
public interface CourseCategoryService extends IService<CourseCategory>{

    //查询所有分类的树状
    List<CourseCategory> findNodes();

    //保存分类
    void saveCourseCategory(CourseCategory courseCategory);

}
