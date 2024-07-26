package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.QuestionBank;
import com.program.model.vo.BankHaveQuestionSum;
import com.program.model.vo.PageResponse;
import com.program.model.vo.QuestionVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface QuestionBankService extends IService<QuestionBank> {

    PageResponse<BankHaveQuestionSum> getBankHaveQuestionSumByType(String bankName, String createPerson,Integer pageNo, Integer pageSize);

    List<QuestionVo> getQuestionsByBankId(Integer bankId);

    List<QuestionVo> getQuestionByBankIdAndType(Integer bankId, Integer type);

    List<QuestionBank> getAllQuestionBanks(String createPerson);

    void addQuestionToBank(String questionIds, String banks);

    void removeBankQuestion(String questionIds, String banks);

    void deleteQuestionBank(String ids);

    void addQuestionBank(QuestionBank questionBank, HttpServletRequest request);
}
