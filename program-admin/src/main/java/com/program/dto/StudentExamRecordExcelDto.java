package com.program.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.program.entity.ExamRecord;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@HeadRowHeight(20)
@ContentRowHeight(15)
@ColumnWidth(30)
public class StudentExamRecordExcelDto implements Serializable {

    @ExcelProperty("考试名称")
    private String examName;

    @ExcelProperty("学生名称")
    private String studentName;

    @ExcelProperty("客观成绩")
    private int objectiveScore;

    @ExcelProperty("主观成绩")
    private int subjectiveScore;

    @ExcelProperty("总得分")
    private int totalScore;

    @ExcelProperty("考试时间")
    private Date examTime;

    public static StudentExamRecordExcelDto from(ExamRecord record, String examName, String studentName) {
        return StudentExamRecordExcelDto.builder()
                .examName(examName)
                .studentName(studentName)
                .objectiveScore(record.getLogicScore())
                .subjectiveScore(Math.max(record.getTotalScore() - record.getLogicScore(), 0))
                .totalScore(record.getTotalScore())
                .examTime(record.getExamTime())
                .build();
    }

}
