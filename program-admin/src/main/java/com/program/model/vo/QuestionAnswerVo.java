package com.program.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linxf
 * @date 2024/7/29
 */
@Data
public class QuestionAnswerVo {

    @ExcelProperty("问题内容*")
    private String quContent;

    @ExcelProperty("题目所有答案")
    private String allOption;

    @ApiModelProperty(value = "正确的答案的索引", example = "allOption[index]")
    @ExcelProperty("答案*")
    private String trueOption;

    @ApiModelProperty(value = "问题相关的图片", example = "imageUrl")
    @ExcelProperty("题目图片")
    private String image;

    @ApiModelProperty(value = "题目解析", example = "题目解析")
    @ExcelProperty("题目解析")
    private String questionAnalysis;

    @ApiModelProperty(value = "答案的图片列表", example = "img1URl, img2URl")
    @ExcelProperty("答案图片")
    private String images;

    @ApiModelProperty(value = "答案解析", example = "1+1=2")
    private String answerAnalysis;
}
