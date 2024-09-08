package com.program.model.dict;

/**
 * @author linxf
 * @date 2024/9/8
 */
public interface DictFileType {
    /**
     * 图片
     */
    Integer IMAGE = 1;
    /**
     * 视频
     */
    Integer VIDEO = 2;
    /**
     * 文档
     */
    Integer DOCUMENT = 3;

    /**
     * 图片类型
     */
    String IMAGE_TYPE = "image";
    /**
     * 视频类型
     */
    String VIDEO_TYPE = "video";

}
