package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.StudentExamRecordExcelDto;
import com.program.model.entity.Exam;
import com.program.model.vo.AddExamByBankVo;
import com.program.model.vo.AddExamByQuestionVo;
import com.program.model.vo.ExamQueryVo;
import com.program.model.vo.PageResponse;

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
