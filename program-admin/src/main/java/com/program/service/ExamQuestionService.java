package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.entity.ExamQuestion;


public interface ExamQuestionService extends IService<ExamQuestion> {

    ExamQuestion getExamQuestionByExamId(Integer examId);

}
