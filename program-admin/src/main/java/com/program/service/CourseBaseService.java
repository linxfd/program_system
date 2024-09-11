package com.program.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.CourseBaseDto;
import com.program.model.dto.CourseDto;
import com.program.model.entity.CourseBase;
import com.program.model.vo.CommonResult;
import com.program.model.vo.CourseInfoVO;
import com.program.model.vo.CourseUnitVo;
import com.program.model.vo.PageResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lxf
 * @date 2024-09-05
 */
public interface CourseBaseService extends IService<CourseBase>{

    // 分页查询
    PageResponse<CourseBase> list(CourseBaseDto courseBaseDto);

    // 添加课程
    void addCategory(CourseDto courseDto, HttpServletRequest request);

    // 查询课程审核信息
    List<CourseUnitVo> queryAudit(Integer id);

    //// 查询需要编辑的课程信息
    CourseDto getCategoryInfo(Integer id);


    // 修改课程
    void updateCourse(CourseDto courseDto, HttpServletRequest request);

    // 删除课程
    void deleteCourse(Integer id);

    //
    void handle(Integer type, String ids);

    //发布/下架
    CommonResult announce(Integer id);

    // 查询课程详情和视频集合
    CourseInfoVO getCourseInfo(Integer id);
}
