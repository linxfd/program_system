<template>
  <el-container>
    <el-header height="180">
      <el-card style="height: 180px">
        <span class="examName">{{ examInfo.examName }}</span>
        <span class="examTime">{{ examRecord.examTime }}</span>

        <el-row style="margin-top: 55px;">
          <el-tooltip
            class="item"
            effect="dark"
            content="包括(单选,多选,判断题)"
            placement="top-start"
          >
            <span style="font-weight: 800;font-size: 17px">
              逻辑题得分: {{ examRecord.logicScore }}分</span>
          </el-tooltip>

          <el-tooltip
            class="item"
            effect="dark"
            content="简答题与逻辑题"
            placement="top-start"
          >
            <span style="float: right;font-weight: 800;font-size: 17px">
              总分: {{ examInfo.totalScore }}分</span>
          </el-tooltip>
        </el-row>
        <el-button
          @click="creditDialog = true"
          size="small"
          style="margin-top: 15px"
          type="primary"
        >
          查看诚信截图
        </el-button>
      </el-card>
    </el-header>

    <el-main>
      <el-card>
        <!--题目信息-->
        <div
          v-for="(item,index) in questionInfo"
          :key="index"
          style="margin-top: 15px"
        >
          <div>
            <i class="num">{{ index + 1 }}</i>
            <span v-if="item.questionType === 1">【单选题】</span>
            <span v-else-if="item.questionType === 2">【多选题】</span>
            <span v-else-if="item.questionType === 3">【判断题】</span>
            <span v-else>【简答题】</span>
            <span>{{ item.questionContent }}:</span>
            <span style="color: red;font-style: italic;font-weight: 400;">&nbsp;
              {{ questionScore.get(String(item.questionId)) }}分</span>
          </div>
          <!--题目中的配图-->
          <img
            v-for="(url, index1) in item.images"
            :key="index1"
            :src="url"
            title="点击查看大图"
            alt="题目图片"
            style="width: 100px;height: 100px;cursor: pointer"
            @click="showBigImg(url)"
          >

          <!--单选 和 判断 的答案列表-->
          <div
            style="margin-top: 25px"
            v-show="item.questionType === 1 || item.questionType === 3"
          >
            <div class="el-radio-group">
              <label
                v-for="(i2,index2) in item.answer"
                :key="index2"
                :class="String(index2) === userAnswer[index] && i2.isTrue === 'true' ?
                  'activeAndTrue' : String(index2) === userAnswer[index] ? 'active' :
                    i2.isTrue === 'true' ? 'true' : ''"
              >
                <span>{{ optionName[index2] + '、' + i2.answer }}</span>
                <div v-if="i2.images !== null">
                  <img
                  style="position: absolute;left:100%;top:50%;transform: translateY(-50%);
                  width: 40px;height: 40px;float: right;cursor: pointer;"
                  title="点击查看大图"
                  
                  v-for="(i3,index3) in i2.images"
                  :key="index3"
                  :src="i3"
                  alt=""
                  @click="showBigImg(i3)"
                >
                </div>
              </label>
            </div>
          </div>

          <!--多选的答案列表-->
          <div
            style="margin-top: 25px"
            v-show="item.questionType === 2"
          >
            <div class="el-radio-group">
              <label
                v-for="(i2,index2) in item.answer"
                :key="index2"
                :class="(userAnswer[index]+'').indexOf(index2+'') !== -1 && i2.isTrue === 'true'
                  ? 'activeAndTrue' : (userAnswer[index]+'').indexOf(index2+'') !== -1 ? 'active' :
                    i2.isTrue === 'true' ? 'true' : ''"
              >
                <span>{{ optionName[index] + '、' + i2.answer }}</span>
                <div v-if="i2.images !== null">
                  <img
                  style="position: absolute;left:100%;top:50%;transform: translateY(-50%);
                  width: 40px;height: 40px;float: right;cursor: pointer;"
                  title="点击查看大图"
                  
                  v-for="(i3,index3) in i2.images"
                  :key="index3"
                  :src="i3"
                  alt=""
                  @click="showBigImg(i3)"
                >
                </div>
              </label>
            </div>
          </div>

          <!--简答题的答案-->
          <div
            style="margin-top: 25px"
            v-if="item.questionType === 4"
          >
            <div class="ques-analysis">
              <h3 style="font-weight: 400">
                我的回答：
              </h3>
              <p style="font-weight: 400;color: orange">
                {{ userAnswer[index] }}
              </p>
            </div>
            <span>评分：</span>
            <el-input-number
              v-model="item.score"
              :min="0"
              :max="parseInt(questionScore.get(String(item.questionId)))"
            />
          </div>
        </div>
      </el-card>
      <el-button
        size="small"
        style="margin-top: 25px"
        type="primary"
        @click="uploadMarkExam"
      >
        提交阅卷
      </el-button>
    </el-main>

    <!--图片回显-->
    <el-dialog
      :visible.sync="bigImgDialog"
      @close="bigImgDialog = false"
    >
      <img
        style="width: 100%"
        :src="bigImgUrl"
      >
    </el-dialog>

    <!--诚信考试图片-->
    <el-dialog
      :visible.sync="creditDialog"
      @close="creditDialog = false"
    >
      <img
        style="width: 100%"
        :src="item"
        v-for="(item,index) in examRecord.creditImgUrl.split(',')"
        :key="index"
      >
    </el-dialog>
  </el-container>
</template>

<script>
import exam from '@/api/exam'
import markExam from '@/api/markExam'
import question from '@/api/question'

export default {
  name: 'MarkExamPage',
  data () {
    return {
      // 考试记录信息
      examRecord: {},
      // 考试的信息
      examInfo: {},
      // 当前考试的题目
      questionInfo: [],
      // 页面加载
      loading: {},
      // 答案的选项名abcd数据
      optionName: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
      // 图片回显的url
      bigImgUrl: '',
      // 图片回显的对话框是否显示
      bigImgDialog: false,
      // 用户回答的答案
      userAnswer: [],
      // 单题的分值
      questionScore: new Map(),
      // 诚信考试的图片的对话框
      creditDialog: false
    }
  },
  props: ['tagInfo'],
  created () {
    // 一创建就改变头部的面包屑
    this.$emit('giveChildChangeBreakInfo', '考试结果', '考试结果')
    this.createTagsInParent()
    this.getExamRecord()

    // 页面数据加载的等待状态栏
    this.loading = this.$Loading.service({
      body: true,
      lock: true,
      text: '数据拼命加载中,(*╹▽╹*)',
      spinner: 'el-icon-loading'
    })
  },
  methods: {
    // 向父组件中添加头部的tags标签
    createTagsInParent () {
      let flag = false
      this.tagInfo.map(item => {
        // 如果tags全部符合
        if (item.name === '批阅试卷' && item.url === this.$route.path) {
          flag = true
        } else if (item.name === '批阅试卷' && item.url !== this.$route.path) {
          this.$emit('updateTagInfo', '批阅试卷', this.$route.path)
          flag = true
        }
      })
      if (!flag) this.$emit('giveChildAddTag', '批阅试卷', this.$route.path)
    },
    // 查询用户当时考试的信息
    async getExamRecord () {
      await exam.getExamRecordById(this.$route.params.recordId).then((resp) => {
        if (resp.code === 200) {
          this.examRecord = resp.data
          this.getExamInfoById(resp.data.examId)
          this.userAnswer = resp.data.userAnswers.split('-')
          // 获取单题的分值
          this.getQuestionScore(resp.data.examId)
          // 获取所有题目信息
          this.getQuestionInfoByIds(resp.data.questionIds)
          // 数据加载完毕
          this.loading.close()
        }
      })
    },
    // 根据考试id查询考试信息
    getExamInfoById (examId) {
      exam.getExamInfoById({ examId: examId }).then((resp) => {
        if (resp.code === 200) this.examInfo = resp.data
      })
    },
    // 根据ids查询题目信息
    async getQuestionInfoByIds (questionIds) {
      await question.getQuestionByIds({ ids: questionIds }).then((resp) => {
        if (resp.code === 200) {
          this.questionInfo = resp?.data?.data || []
          this.questionInfo.forEach(q => {
            if (q.questionType === 4) {
              q.score = 0
            }
          })
          // 重置问题的顺序 单选 多选 判断 简答
          this.questionInfo = this.questionInfo.sort(function (a, b) {
            return a.questionType - b.questionType
          })
        }
      })
    },
    // 点击展示高清大图
    showBigImg (url) {
      this.bigImgUrl = url
      this.bigImgDialog = true
    },
    // 根据考试id查询考试中每一题的分数
    async getQuestionScore (examId) {
      await exam.getExamQuestionByExamId(examId).then((resp) => {
        if (resp.code === 200) {
          // 设置单题分值给map
          const scores = resp.data.scores.split(',')
          resp.data.questionIds.split(',').forEach((item, index) => {
            // this.$set(this.questionScore, item, scores[index])
            this.questionScore.set(item, scores[index])
          })
        }
      })
    },
    // 提交阅卷
    uploadMarkExam () {
      // 客观题的分数
      let otherScore = 0
      this.questionInfo.forEach(item => {
        if (item.questionType === 4) {
          otherScore += item.score
        }
      })
      const totalScore = this.examRecord.logicScore + otherScore
      markExam.markExam({
        totalScore: totalScore,
        examRecordId: this.$route.params.recordId
      }).then((resp) => {
        if (resp.code === 200) {
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
          this.$router.push('/markManage')
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/css/teacher/markExamPage";
</style>
