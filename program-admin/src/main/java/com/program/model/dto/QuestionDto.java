package com.program.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.program.model.vo.QuestionVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author linxf
 * @date 2024/7/29
 */
@ApiModel("题目DTO")
@Data
public class QuestionDto {

    @ApiModelProperty(value = "问题类型", example = " 1单选 2多选 3判断 4简答")
    private Integer questionType;

    @ApiModelProperty(value = "问题难度", example = "1")
    private Integer questionLevel;

    @ApiModelProperty(value = "所属题库的id", example = "1,2")
    private Integer[]   bankId;

    @ApiModelProperty(value = "创建人的username", example = "wzz")
    private String createPerson;

    @ApiModelProperty(value = "所属题库的名称", example = "小学题库")
    private String quBankName;

}
