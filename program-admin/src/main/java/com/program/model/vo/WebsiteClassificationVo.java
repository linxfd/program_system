package com.program.model.vo;

import com.program.model.entity.WebsiteClassification;
import lombok.Data;

/**
 * @author linxf
 * @date 2024/7/31
 */
@Data
public class WebsiteClassificationVo extends WebsiteClassification {
    // 当前页码
    private Integer pageNum;
    // 每页显示条数
    private Integer pageSize;
}
