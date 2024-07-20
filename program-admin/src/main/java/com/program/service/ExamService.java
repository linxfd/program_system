package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.dto.StudentExamRecordExcelDto;
import com.program.entity.Exam;
import com.program.vo.AddExamByBankVo;
import com.program.vo.AddExamByQuestionVo;
import com.program.vo.ExamQueryVo;
import com.program.vo.PageResponse;

import java.util.List;


public interface ExamService extends IService<Exam> {

    PageResponse<Exam> getExamPage(ExamQueryVo examQueryVo);

    AddExamByQuestionVo getExamInfoById(Integer examId);

    void operationExam(Integer type, String ids);

    void addExamByBank(AddExamByBankVo addExamByBankVo);

    void addExamByQuestionList(AddExamByQuestionVo addExamByQuestionVo);

    void updateExamInfo(AddExamByQuestionVo addExamByQuestionVo);

    List<String> getExamPassRateEchartData();

    List<String> getExamNumbersEchartData();

    List<StudentExamRecordExcelDto> getAllStudentScoreByExamId(Integer examId);

}
