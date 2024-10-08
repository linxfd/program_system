package com.program.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQueryVo {

    private Integer examType;

    private String startTime;

    private String endTime;

    private String examName;

    private String createPerson;

    @NotNull
    private Integer pageNo;

    @NotNull
    private Integer pageSize;

}
