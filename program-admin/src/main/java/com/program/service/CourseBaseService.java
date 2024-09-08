package com.program.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.CourseBaseDto;
import com.program.model.entity.CourseBase;
import com.program.model.vo.PageResponse;

/**
 * @author lxf
 * @date 2024-09-05
 */
public interface CourseBaseService extends IService<CourseBase>{

    // 分页查询
    PageResponse<CourseBase> list(CourseBaseDto courseBaseDto);
}
