package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.entity.QuestionBank;
import com.program.vo.BankHaveQuestionSum;
import com.program.vo.PageResponse;
import com.program.vo.QuestionVo;

import java.util.List;


public interface QuestionBankService extends IService<QuestionBank> {

    PageResponse<BankHaveQuestionSum> getBankHaveQuestionSumByType(String bankName, Integer pageNo, Integer pageSize);

    List<QuestionVo> getQuestionsByBankId(Integer bankId);

    List<QuestionVo> getQuestionByBankIdAndType(Integer bankId, Integer type);

    List<QuestionBank> getAllQuestionBanks();

    void addQuestionToBank(String questionIds, String banks);

    void removeBankQuestion(String questionIds, String banks);

    void deleteQuestionBank(String ids);

    void addQuestionBank(QuestionBank questionBank);
}
