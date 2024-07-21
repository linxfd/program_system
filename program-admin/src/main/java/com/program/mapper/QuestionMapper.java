package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.entity.Question;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionMapper extends BaseMapper<Question> {
}
