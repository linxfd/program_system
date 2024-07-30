package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.QuestionDto;
import com.program.model.entity.Question;
import com.program.model.vo.PageResponse;
import com.program.model.vo.QuestionAnswerVo;
import com.program.model.vo.QuestionVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface QuestionService extends IService<Question> {

    PageResponse<Question> getQuestion(String questionType, String questionBank, String questionContent, String createPerson,Integer pageNo, Integer pageSize);

    QuestionVo getQuestionVoById(Integer id);

    PageResponse<QuestionVo> getQuestionVoByIds(List<Integer> ids);

    void deleteQuestionByIds(String questionIds);

    void addQuestion(QuestionVo questionVo);

    void updateQuestion(QuestionVo questionVo);

    // 导入题目和对应答案
    void importQurstion(QuestionDto questionDto, MultipartFile file);

    // 导出题目和对应答案
    PageResponse<QuestionAnswerVo> getQuestionExportHand(String questionType, String questionBank, String questionContent, String createPerson, Integer pageNo, Integer pageSize);
}
