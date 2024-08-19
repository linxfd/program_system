package com.program.model.dto;

import com.program.model.entity.Points;
import lombok.Data;

/**
 * @author linxf
 * @date 2024/8/19
 */
@Data
public class PointsDto extends Points {
    // 当前页码
    private Integer pageNum;
    // 每页显示条数
    private Integer pageSize;
}
