package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.ExamRecord;
import com.program.model.vo.PageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ExamRecordService extends IService<ExamRecord> {

    PageResponse<ExamRecord> getUserGrade(String username, Integer examId, Integer pageNo, Integer pageSize);

    void createExamCertificate(HttpServletResponse response, String examName, Integer examRecordId);

    ExamRecord getExamRecordById(Integer recordId);

    Integer addExamRecord(ExamRecord examRecord, HttpServletRequest request);

    PageResponse<ExamRecord> getExamRecord(Integer examId, String createPerson,Integer pageNo, Integer pageSize);

    void setObjectQuestionScore(Integer totalScore, Integer examRecordId);
}
