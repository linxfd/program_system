package com.program.model.vo;

import com.program.model.entity.CourseUnit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linxf
 * @date 2024/9/9
 */
@Data
public class CourseUnitVo extends CourseUnit {
    @ApiModelProperty(value = "视频审核状态，1:未审核 2:不通过 3通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "若是视频，表示视频时长", example = "16:12")
    private String videoDuration;
    
}
