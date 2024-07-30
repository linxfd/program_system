package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.dto.QuestionDto;
import com.program.model.entity.QuestionBank;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionBankMapper extends BaseMapper<QuestionBank> {

    List<String> getNamedQuery(QuestionDto questionDto);
}
