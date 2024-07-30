package com.program.utils.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.mapper.AnswerMapper;
import com.program.mapper.QuestionMapper;
import com.program.model.dict.DictAlphabetArray;
import com.program.model.dto.QuestionDto;
import com.program.model.entity.Answer;
import com.program.model.entity.Question;
import com.program.model.entity.User;
import com.program.model.vo.QuestionAnswerVo;
import com.program.model.vo.TokenVo;
import com.program.utils.JwtUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linxf
 * @date 2024/7/29
 */
public class  ExcelListener<T> extends AnalysisEventListener<T> {

    //获取mapper对象，因为Listener不能让spring管理所以不能@Autowired
    private QuestionMapper questionMapper;
    private AnswerMapper answerMapper;
    private QuestionDto questionDto;

    public ExcelListener(QuestionMapper questionMapper, AnswerMapper answerMapper,QuestionDto questionDto){
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
        this.questionDto = questionDto;
    }

    /**
     每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;


    //可以通过实例获取该值
    private List<QuestionAnswerVo> datas = new ArrayList<>();

    public List<QuestionAnswerVo> getDatas() {
        return datas;
    }

    /**
     * 逐行读取数据
     * @param o     从第二行开始读取，把每行读取到的内容封装到o对象里面
     * @param analysisContext
     */
    @Override
    public void invoke(T o, AnalysisContext analysisContext) {  // 每解析一行数据就会调用一次该方法
        QuestionAnswerVo data = (QuestionAnswerVo) o;

        datas.add(data);//数据存储到list，供批量处理，或后续自己业务逻辑处理。

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (datas.size() >= BATCH_COUNT) {
            this.saveData();
            // 存储完成清理 list
            datas = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    // 保存到数据库
    @Transactional
    public void saveData() {
        for (QuestionAnswerVo data : datas) {
            Question question = new Question();
            question.setQuContent(data.getQuContent());
            question.setCreatePerson(questionDto.getCreatePerson());
            question.setQuType(questionDto.getQuestionType());
            question.setLevel(questionDto.getQuestionLevel());
            question.setImage(data.getImage());
            //注意字符串传化
            List<Integer> numberList = Arrays.asList(questionDto.getBankId());
            String join = String.join(",", numberList.stream().map(Object::toString).collect(Collectors.toList()));
            question.setQuBankId(join);
            question.setQuBankName(questionDto.getQuBankName());
            question.setAnalysis(data.getQuestionAnalysis());
            question.setCreateTime(new Date());
            questionMapper.insert(question);

            Answer answer = new Answer();
            answer.setAllOption(data.getAllOption());
            if (data.getImages() != null) {
                answer.setImages(data.getImages());
            }else {
                answer.setImages("");
            }
            answer.setAnalysis(data.getAnswerAnalysis());
            answer.setQuestionId(question.getId());
            char trueOption = data.getTrueOption().charAt(0);
            int index = 0;
            if (trueOption>=97){
                index = trueOption - 'a';
            }else {
                index = trueOption - 'A';
            }
            answer.setTrueOption(String.valueOf(index));
            answerMapper.insert(answer);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // excel解析完毕以后需要执行的代码，有可能后面的数据不是100的倍数，还没有进行保存数据
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        this.saveData();
    }

}
