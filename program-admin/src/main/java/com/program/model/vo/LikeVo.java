package com.program.model.vo;

import lombok.Data;

/**
 * @author linxf
 * @date 2024/9/13
 */
@Data
public class LikeVo {

    //点赞状态
    private Boolean isLike;

    //点赞数量
    private Long likeCount;
}
