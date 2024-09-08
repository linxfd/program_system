package com.program.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.CourseUnitMapper;
import com.program.model.entity.CourseUnit;
import com.program.service.CourseUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author linxf
 * @date 2024/9/9
 */
@Service
public class CourseUnitServiceImpl extends ServiceImpl<CourseUnitMapper, CourseUnit> implements CourseUnitService {

    @Autowired
    private CourseUnitMapper courseUnitMapper;

}
