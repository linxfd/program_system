package com.program.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.entity.Answer;
import com.program.mapper.AnswerMapper;
import com.program.service.AnswerService;
import org.springframework.stereotype.Service;


@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
}
