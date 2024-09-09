package com.program.model.dto;

import com.program.model.entity.CourseBase;
import com.program.model.entity.CourseCategory;
import com.program.model.entity.CourseUnit;
import lombok.Data;

import java.util.List;

/**
 * @author linxf
 * @date 2024/9/9
 */
@Data
public class CourseDto {

    CourseBase courseBase;
    List<CourseUnit> cards;
}
