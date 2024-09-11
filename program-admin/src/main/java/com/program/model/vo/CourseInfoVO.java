package com.program.model.vo;

import com.program.model.entity.CourseBase;
import com.program.model.entity.CourseUnit;
import lombok.Data;

import java.util.List;

/**
 * @author linxf
 * @date 2024/9/11
 */
@Data
public class CourseInfoVO {
    /**
     * 课程基本信息
     */
    private CourseBase courseBase;

    /**
     * 课程单元集合
     */
    private List<CourseUnitVo> courseUnitList;

    public void setCourseUnits(List<CourseUnitVo> courseUnits) {
        this.courseUnitList = courseUnits;
    }
}
