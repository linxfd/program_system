package com.program.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.model.dto.QuestionDto;
import com.program.model.entity.Answer;
import com.program.model.entity.Question;
import com.program.mapper.AnswerMapper;
import com.program.mapper.QuestionBankMapper;
import com.program.mapper.QuestionMapper;
import com.program.model.vo.QuestionAnswerVo;
import com.program.service.QuestionService;
import com.program.utils.EmptyUtil;
import com.program.utils.RedisUtil;
import com.program.model.vo.PageResponse;
import com.program.model.vo.QuestionVo;
import com.program.utils.listener.ExcelListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.program.utils.CommonUtils.setEqualsQueryWrapper;
import static com.program.utils.CommonUtils.setLikeWrapper;


@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private final QuestionMapper questionMapper;

    private final QuestionBankMapper questionBankMapper;

    private final AnswerMapper answerMapper;

    private final RedisUtil redisUtil;

    @Override
    public PageResponse<Question> getQuestion(String questionType, String questionBank, String questionContent, String createPerson,Integer pageNo, Integer pageSize) {
        IPage<Question> questionPage = new Page<>(pageNo, pageSize);

        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        Map<String, Object> likeQueryParams = new HashMap<>(2);
        likeQueryParams.put("qu_bank_name", questionBank);
        likeQueryParams.put("qu_content", questionContent);
        setLikeWrapper(wrapper, likeQueryParams);
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("qu_type", questionType));

        if(!EmptyUtil.isNotUtils(createPerson)){
            wrapper.eq("create_person", createPerson);
        }
        questionPage = questionMapper.selectPage(questionPage, wrapper);
        return PageResponse.<Question>builder()
                .data(questionPage.getRecords())
                .total(questionPage.getTotal())
                .build();
    }

//    @Cache(prefix = "questionVo", suffix = "#id", ttl = 5, timeUnit = TimeUnit.HOURS)
    @Override
    public QuestionVo getQuestionVoById(Integer id) {
        Question question = questionMapper.selectById(id);
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("question_id", id));
        return buildQuestionVoByQuestionAndAnswer(question, answer);
    }

    @Override
    public PageResponse<QuestionVo> getQuestionVoByIds(List<Integer> ids) {
        List<Question> questions = questionMapper.selectBatchIds(ids);
        List<Answer> answers = answerMapper.selectList(new QueryWrapper<Answer>().in("question_id", ids));
        List<QuestionVo> questionVos = questions.stream()
                .map(question -> {
                    Answer currentQuestionAnswer = answers.stream()
                            .filter(answer -> answer.getQuestionId().equals(question.getId()))
                            .findFirst()
                            .orElse(null);
                    return buildQuestionVoByQuestionAndAnswer(question, currentQuestionAnswer);
                }).collect(Collectors.toList());
        return PageResponse.<QuestionVo>builder()
                .data(questionVos)
                .total(questionVos.size())
                .build();
    }

    @Override
    public void deleteQuestionByIds(String questionIds) {
        String[] ids = questionIds.split(",");
        Map<String, Object> map = new HashMap<>();
        for (String id : ids) {
            map.clear();
            map.put("question_id", id);
            //  1. 删除数据库的题目信息
            questionMapper.deleteById(Integer.parseInt(id));
            // 2. 删除答案表对应当前题目id的答案
            answerMapper.deleteByMap(map);
            //  2. 移除redis缓存
            redisUtil.del("questionVo:" + id);
        }
        //  清楚题库的缓存
        redisUtil.del("questionBanks");
    }

    @Transactional
    @Override
    public void addQuestion(QuestionVo questionVo) {
        Question question = new Question();
        // 设置基础字段
        question.setQuType(questionVo.getQuestionType());
        setQuestionField(question, questionVo);
        // 设置题目插图
        if (questionVo.getImages().length != 0) {
            String QuImages = Arrays.toString(questionVo.getImages());
            question.setImage(QuImages.substring(1, QuImages.length() - 1).replaceAll(" ", ""));
        }
        buildBankName(questionVo, question);
        questionMapper.insert(question);
        Integer currentQuId = question.getId();
        // 设置答案对象
        StringBuilder multipleChoice = new StringBuilder();
        if (questionVo.getQuestionType() != 4) {// 不为简答题
            Answer answer = new Answer();
            answer.setQuestionId(currentQuId);
            StringBuilder imgs = new StringBuilder();
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < questionVo.getAnswer().length; i++) {
                if (questionVo.getAnswer()[i].getImages().length > 0) {// 如果该选项有一张图片信息
                    imgs.append(questionVo.getAnswer()[i].getImages()[0]).append(",");
                }
                buildAnswer(answers, questionVo, i, multipleChoice, answer);
            }
            buildMultiQuestionAnswer(questionVo, multipleChoice, answer, imgs, answers);
            answerMapper.insert(answer);
        }
        clearQuestionBankCache(questionVo, redisUtil);
    }

    @Override
    public void updateQuestion(QuestionVo questionVo) {
        Question question = new Question();
        // 设置基础字段
        question.setQuType(questionVo.getQuestionType());
        question.setId(questionVo.getQuestionId());
        setQuestionField(question, questionVo);
        // 设置题目插图
        if (questionVo.getImages() != null && questionVo.getImages().length != 0) {
            String QuImages = Arrays.toString(questionVo.getImages());
            question.setImage(QuImages.substring(1, QuImages.length() - 1).replaceAll(" ", ""));
        }
        buildBankName(questionVo, question);
        // 更新
        questionMapper.update(question, new UpdateWrapper<Question>().eq("id", questionVo.getQuestionId()));
        // 设置答案对象
        StringBuilder multipleChoice = new StringBuilder();
        if (questionVo.getQuestionType() != 4) {// 不为简答题
            Answer answer = new Answer();
            answer.setQuestionId(questionVo.getQuestionId());
            StringBuilder imgs = new StringBuilder();
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < questionVo.getAnswer().length; i++) {
                if (questionVo.getAnswer()[i].getImages() != null && questionVo.getAnswer()[i].getImages().length > 0) {// 如果该选项有一张图片信息
                    imgs.append(questionVo.getAnswer()[i].getImages()[0]).append(",");
                }
                buildAnswer(answers, questionVo, i, multipleChoice, answer);
            }
            buildMultiQuestionAnswer(questionVo, multipleChoice, answer, imgs, answers);
            answerMapper.update(answer, new UpdateWrapper<Answer>().eq("question_id", questionVo.getQuestionId()));
            redisUtil.del("questionVo:" + questionVo.getQuestionId());

            clearQuestionBankCache(questionVo, redisUtil);
        }
    }

    @Override
    public void importQurstion(QuestionDto questionDto, MultipartFile file) {
        List<String> questionsNames = questionBankMapper.getNamedQuery(questionDto);
        questionDto.setQuBankName(String.join(",", questionsNames));
        try {
            //创建监听器对象，传递mapper对象
            ExcelListener<QuestionAnswerVo> excelListener = new ExcelListener<>(questionMapper,answerMapper,questionDto);
            //调用read方法读取excel数据
            EasyExcel.read(file.getInputStream(),
                    QuestionAnswerVo.class,
                    excelListener).sheet().doRead();
        } catch (Exception e) {
            throw new RuntimeException("读取题目文件失败");
        }
    }

    @Override
    public PageResponse<QuestionAnswerVo> getQuestionExportHand(String questionType, String questionBank, String questionContent, String createPerson, Integer pageNo, Integer pageSize) {
        IPage<Question> questionPage = new Page<>(pageNo, pageSize);

        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        Map<String, Object> likeQueryParams = new HashMap<>(2);
        likeQueryParams.put("qu_bank_name", questionBank);
        likeQueryParams.put("qu_content", questionContent);
        setLikeWrapper(wrapper, likeQueryParams);
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("qu_type", questionType));

        if(!EmptyUtil.isNotUtils(createPerson)){
            wrapper.eq("create_person", createPerson);
        }
        questionPage = questionMapper.selectPage(questionPage, wrapper);

        List<QuestionAnswerVo>  questionAnswerVoList = new ArrayList<>();
        for (Question record : questionPage.getRecords()) {
            QuestionAnswerVo questionAnswerVo1 = new QuestionAnswerVo();
            questionAnswerVo1.setQuContent(record.getQuContent());
            Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("question_id", record.getId()));
            if(answer!=null){
                questionAnswerVo1.setAllOption(answer.getAllOption());
                int index = answer.getTrueOption().charAt(0) + 31;
                questionAnswerVo1.setTrueOption(String.valueOf(index));
                questionAnswerVo1.setImages(answer.getImages());
                questionAnswerVo1.setAnswerAnalysis(answer.getAnalysis());
            }
            questionAnswerVo1.setImage(record.getImage());
            questionAnswerVo1.setQuestionAnalysis(record.getAnalysis());
            questionAnswerVoList.add(questionAnswerVo1);
        }
        return PageResponse.<QuestionAnswerVo>builder()
                .data(questionAnswerVoList)
                .total(questionPage.getTotal())
                .build();

    }

    private void clearQuestionBankCache(QuestionVo questionVo, RedisUtil redisUtil) {
        for (Integer bankId : questionVo.getBankId()) {
            // remove cache(all question in bank cache)
            redisUtil.del("questionBankQuestion:" + bankId);
        }
    }

    private void buildAnswer(StringBuilder answers, QuestionVo questionVo, int i, StringBuilder multipleChoice, Answer answer) {
        answers.append(questionVo.getAnswer()[i].getAnswer()).append(",");
        // 设置对的选项的下标值
        if (questionVo.getQuestionType() == 2) {// 多选
            if (questionVo.getAnswer()[i].getIsTrue().equals("true")) multipleChoice.append(i).append(",");
        } else {// 单选和判断 都是仅有一个答案
            if (questionVo.getAnswer()[i].getIsTrue().equals("true")) {
                answer.setTrueOption(i + "");
                answer.setAnalysis(questionVo.getAnswer()[i].getAnalysis());
            }
        }
    }

    private void buildMultiQuestionAnswer(QuestionVo questionVo, StringBuilder multipleChoice, Answer answer, StringBuilder imgs, StringBuilder answers) {
        if (questionVo.getQuestionType() == 2)
            answer.setTrueOption(multipleChoice.substring(0, multipleChoice.toString().length() - 1));
        String handleImgs = imgs.toString();
        String handleAnswers = answers.toString();
        if (handleImgs.length() != 0) handleImgs = handleImgs.substring(0, handleImgs.length() - 1);
        if (handleAnswers.length() != 0) handleAnswers = handleAnswers.substring(0, handleAnswers.length() - 1);

        // 设置答案的图片
        answer.setImages(handleImgs);
        // 设置所有的选项
        answer.setAllOption(handleAnswers);
    }

    private void buildBankName(QuestionVo questionVo, Question question) {
        StringBuilder bankNames = new StringBuilder();
        for (Integer integer : questionVo.getBankId()) {
            bankNames.append(questionBankMapper.selectById(integer).getBankName()).append(",");
        }
        String names = bankNames.toString();
        names = names.substring(0, names.length() - 1);
        question.setQuBankName(names);
    }

    private void setQuestionField(Question question, QuestionVo questionVo) {
        question.setCreateTime(new Date());
        question.setLevel(questionVo.getQuestionLevel());
        question.setAnalysis(questionVo.getAnalysis());
        question.setQuContent(questionVo.getQuestionContent());
        question.setCreatePerson(questionVo.getCreatePerson());
        // 设置所属题库
        String bankIds = Arrays.toString(questionVo.getBankId());
        question.setQuBankId(bankIds.substring(1, bankIds.length() - 1).replaceAll(" ", ""));
    }

    private QuestionVo buildQuestionVoByQuestionAndAnswer(Question question, Answer answer) {
        QuestionVo questionVo = new QuestionVo();
        // 设置字段
        questionVo.setQuestionContent(question.getQuContent());
        questionVo.setAnalysis(question.getAnalysis());
        questionVo.setQuestionType(question.getQuType());
        questionVo.setQuestionLevel(question.getLevel());
        questionVo.setQuestionId(question.getId());
        if (question.getImage() != null && !Objects.equals(question.getImage(), "")) {
            questionVo.setImages(question.getImage().split(","));
        }
        questionVo.setCreatePerson(question.getCreatePerson());
        // 设置所属题库
        if (!Objects.equals(question.getQuBankId(), "")) {
            String[] bids = question.getQuBankId().split(",");
            Integer[] bankIds = new Integer[bids.length];
            for (int i = 0; i < bids.length; i++) {
                bankIds[i] = Integer.parseInt(bids[i]);
            }
            questionVo.setBankId(bankIds);
        }
        if (answer != null) {
            String[] allOption = answer.getAllOption().split(",");
            String[] imgs = answer.getImages().split(",");
            QuestionVo.Answer[] qa = new QuestionVo.Answer[allOption.length];
            if (question.getQuType() != 2) {
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    if (i <= imgs.length - 1 && !Objects.equals(imgs[i], ""))
                        answer1.setImages(new String[]{imgs[i]});
                    if (i == Integer.parseInt(answer.getTrueOption())) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            } else {// 多选
                List<Integer> multiTrueOptions = Arrays.stream(answer.getTrueOption().split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    answer1.setImages(imgs);
                    if (multiTrueOptions.contains(i)) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            }
            questionVo.setAnswer(qa);
        }
        return questionVo;
    }

}
