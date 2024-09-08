package com.program.model.dto;

import com.program.model.entity.CourseBase;
import lombok.Data;

/**
 * @author linxf
 * @date 2024/9/5
 */

@Data
public class CourseBaseDto extends CourseBase {

    // 当前页码
    private Integer pageNo;
    // 每页显示条数
    private Integer pageSize;
}
