package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.Question;
import com.program.model.vo.PageResponse;
import com.program.model.vo.QuestionVo;

import java.util.List;


public interface QuestionService extends IService<Question> {

    PageResponse<Question> getQuestion(String questionType, String questionBank, String questionContent, Integer pageNo, Integer pageSize);

    QuestionVo getQuestionVoById(Integer id);

    PageResponse<QuestionVo> getQuestionVoByIds(List<Integer> ids);

    void deleteQuestionByIds(String questionIds);

    void addQuestion(QuestionVo questionVo);

    void updateQuestion(QuestionVo questionVo);

}
