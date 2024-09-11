package com.program.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author linxf
 * @date 2024/9/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文件信息")
@TableName(value = "media_files")
public class MediaFiles {
    @ApiModelProperty(value = "文件id,32位md5值", example = "1137f04b2f44d1b2c37bcb73608864da")
    private String id;

    @ApiModelProperty(value = "文件名称", example = "wzz")
    private String fileName;

    @ApiModelProperty(value = "文件类型（图片，视频、文档）1图片；2视频3文档", example = "1")
    private Integer fileType;

    @ApiModelProperty(value = "媒资文件访问地址")
    private String url;

    @ApiModelProperty(value = "文件类型（图片，视频、文档）1图片；2视频3文档", example = "1")
    private Long fileSize;

    @ApiModelProperty(value = "文件大小", example = "32")
    private String createPerson;

    @ApiModelProperty(value = "若是视频，表示视频时长", example = "16:12")
    private String videoDuration;

    @ApiModelProperty(value = "状态,1:正常，0:不展示")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
