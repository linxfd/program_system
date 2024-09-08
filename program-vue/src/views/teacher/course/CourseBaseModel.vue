<template>
  <el-container>
    <el-header height="220">
      <el-steps
        :active="curStep"
        simple
      >
        <el-step
          title="课程信息"
          icon="el-icon-edit"
        />
        <el-step
          title="视频列表"
          icon="el-icon-lock"
        />
        <el-step
          title="积分设置"
          icon="el-icon-setting"
        />
      </el-steps>

      <el-button
        style="margin-top: 10px"
        v-show="curStep !== 1"
        @click="curStep--"
      >
        上一步
      </el-button>

      <el-button
        style="float:right;margin-top: 10px"
        v-show="curStep !== 3"
        type="primary"
        @click="curStep++"
      >
        下一步
      </el-button>
      <el-button
        style="float:right;margin-top: 10px"
        v-show="curStep === 3"
        type="primary"

      >
        提交
      </el-button>
      <br/>

    </el-header>

    <el-main>
      <!--设置试题信息-->
      <el-card v-show="curStep === 1">
        <div>
          <el-card>
            <h1>课程信息</h1>
            <el-form
              :model="categoryInfo"
              :rules="examInfoRules"
              ref="examInfoForm"
              label-width="100px"
            >
              <el-form-item
                label="课程名称"
                prop="name"
              >
                <el-input v-model="categoryInfo.name" />
              </el-form-item>

              <el-form-item
                label="课程介绍"
                prop="description"
              >
                <el-input v-model="categoryInfo.description" 
                type="textarea"
                :rows="4"
                />
              </el-form-item>

              <el-form-item
                label="课程分类"
              >
                <el-cascader  
                  :options="categoryOptions"  
                  :props="cascaderProps"  
                  style="margin-left: 5px"
                  placeholder="搜索分类"
                  v-model="selectedCategories"  
                  clearable  
                  @change="handleChange"  
                />
              </el-form-item>
              
              
              <el-form-item
                label="课程首页图片"
              >
                <el-upload
                    ref="upload"
                    :action="uploadImageUrl + '/teacher/uploadQuestionImage'"
                    name="file"
                    :headers="headers"
                    :before-upload="beforeUpload"
                    :on-success="importFile"
                    v-loading="Uploadloading"
                  >
                    <div  class="placeholder upload-placeholder">
                      <p v-if="!this.categoryInfo.pic">点击上传图片</p>
                      <img v-else :src="this.categoryInfo.pic" alt=""  class="preview-image">
                    </div>
                    
                </el-upload>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-card>


      <el-card v-show="curStep === 2">
        <div>
          <el-card>
            <h1>视频信息</h1>
            
          </el-card>
        </div>
      </el-card>

      <!--设置考试权限-->
      <el-card v-show="curStep === 3">
        <el-radio-group
          v-model="categoryInfo.type"
          size="medium"
        >
          <el-radio
            :label="1"
            border
          >
            完全公开
          </el-radio>
          <el-radio
            :label="2"
            border
          >
            需要密码
          </el-radio>
        </el-radio-group>

        <el-alert
          style="margin-top: 15px"
          :title="categoryInfo.type === 1? '开放的，任何人都可以进行考试！' : '半开放的，知道密码的人员才可以考试！'"
          type="warning"
        />

        <el-input
          style="margin-top: 15px;width: 20%"
          v-model="categoryInfo.password"
          v-show="categoryInfo.type === 2"
          type="password"
          show-password
          placeholder="输入考试密码"
        />
      </el-card>


    </el-main>


  </el-container>
</template>

<script>
import examApi from '@/api/exam'
import question from '@/api/question'
import questionBank from '@/api/questionBank'
import utils from '@/utils/utils'
import { generateSign } from '@/utils/sign'
import category from '@/api/category'
import courseCategory from '@/api/courseCategory'

export default {
  name: 'CourseBaseModel',
  data () {
    return {
      uploadImageUrl: process.env.VUE_APP_UPLOAD_IMAGE_URL,
      // 查询题目的参数
      queryInfo: {
        // 题目类型下拉款所选的内容
        questionType: '',
        questionBank: '',
        questionContent: '',
        pageNo: 1,
        pageSize: 10
      },
      // 题目类型
      questionType: [
        {
          id: 1,
          name: '单选题'
        },
        {
          id: 2,
          name: '多选题'
        },
        {
          id: 3,
          name: '判断题'
        },
        {
          id: 4,
          name: '简答题'
        }
      ],
      // 所有题库信息
      allBank: [],
      // 当前的步骤
      curStep: 1,
      // 考试题目信息
      updateExamQuestion: [],
      // 添加题库的对话框
      showQuestionDialog: false,
      // 对话框中题目表格的加载
      loading: true,
      // 数据预加载
      Uploadloading: false,
      // 页面中的题目列表表格
      pageLoading: true,
      // 所有题目的信息
      questionInfo: [],
      // 所有题目的对话框中表格被选中
      selectedTable: [],
      // 对话框中题目的总数
      total: 0,

      // 当前课程的信息
      categoryInfo: {},
      // 表单验证
      examInfoRules: {
      },
      selectedCategories: [],  
      categoryOptions: [],  
      //分类的配置
      cascaderProps: {
        value: 'id',  // 使用 id 作为唯一标识符  
        label: 'label',  
        children: 'children',  
        // disabled: 'isDisabled',  
        checkStrictly: false,
      },
    }
  },
  props: ['tagInfo'],
  created () {
    // 一创建就改变头部的面包屑
    this.$emit('giveChildChangeBreakInfo', '更新考试', '更新考试')
    this.createTagsInParent()

    // 查询分类数据
    this.fetchData()
    // this.getExamInfo()
    // this.getBankInfo()
  },
  methods: {
    // 向父组件中添加头部的tags标签
    createTagsInParent () {
      let flag = false
      this.tagInfo.map(item => {
        // 如果tags全部符合
        if (item.name === '更新考试' && item.url === this.$route.path) {
          flag = true
        } else if (item.name === '更新考试' && item.url !== this.$route.path) {
          this.$emit('updateTagInfo', '更新考试', this.$route.path)
          flag = true
        }
      })
      if (!flag) this.$emit('giveChildAddTag', '更新考试', this.$route.path)
    },
    // 根据考试的id查询考试的信息和题目的信息
    async getExamInfo () {
      await examApi.getExamInfoById(this.$route.params).then((resp) => {
        if (resp.code === 200) {
          this.categoryInfo = resp.data
          const scores = resp.data.scores.split(',')
          const questionArr = resp.data.questionIds.split(',')
          const questionScoreMap = new Map()
          for (let i = 0; i < questionArr.length; i++) {
            questionScoreMap.set(questionArr[i], scores[i])
          }
          question.getQuestionByIds({ ids: resp.data.questionIds }).then(resp => {
            if (resp.code === 200) {
              (resp?.data?.data || []).forEach(q => {
                this.updateExamQuestion.push({
                  questionId: parseInt(q.questionId),
                  questionType: q.questionType,
                  questionContent: q.questionContent,
                  score: questionScoreMap.get(`${q.questionId}`)
                })
              })
            }
          })
          this.pageLoading = false
        }
      })
    },
    // 自由组卷时添加试题
    showAddDialog () {
      this.showQuestionDialog = true
      this.getQuestionInfo()
    },
    // 获取所有的题库信息
    getBankInfo () {
       const roleId = window.localStorage.getItem('roleId')
      // 如果是老师，则只查询自己的题库，管理员可以查看全部
      let model = {
        pageNo: 1,
        pageSize: 9999
      }
      if(roleId == 2){
        model.createPerson = window.localStorage.getItem('username')
      }
      questionBank.getBankHaveQuestionSumByType(model).then((resp) => {
        if (resp.code === 200) {
          this.allBank = resp.data.data
        } else {
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'error',
            duration: 2000
          })
        }
      })
    },
    // 获取题目信息
    getQuestionInfo () {
       const roleId = window.localStorage.getItem('roleId')
      // 如果是老师，则只查询自己的题库，管理员可以查看全部
      if(roleId == 2){
        this.queryInfo.createPerson = window.localStorage.getItem('username')
      }
      question.getQuestion(this.queryInfo).then((resp) => {
        if (resp.code === 200) {
          this.questionInfo = resp.data.data
          this.total = resp.data.total
          this.loading = false
        } else {
          this.$notify({
            title: 'Tips',
            message: '获取题库信息失败',
            type: 'error',
            duration: 2000
          })
        }
      })
    },
    // 自由组卷时删除试题
    delQuestion (questionId) {
      this.updateExamQuestion.forEach((item, index) => {
        if (item.questionId === questionId) this.updateExamQuestion.splice(index, 1)
      })
    },
    // 题目类型变化
    typeChange (val) {
      this.queryInfo.questionType = val
      this.getQuestionInfo()
    },
    // 题库变化
    bankChange (val) {
      this.queryInfo.questionBank = val
      this.getQuestionInfo()
    },
    // 自由组卷中选中的题目添加进去
    addQuToFree () {
      this.selectedTable.forEach(item => {
        if (!this.updateExamQuestion.some(i2 => {
          return i2.questionId === item.id
        })) { // 不存在有当前题目
          this.updateExamQuestion.push({
            questionId: item.id,
            questionContent: item.quContent,
            questionType: item.quType,
            score: 1
          })
        }
      })
      this.showQuestionDialog = false
    },
    // 处理表格被选中
    handleTableSectionChange (val) {
      this.selectedTable = val
    },
    // 分页页面大小改变
    handleSizeChange (val) {
      this.queryInfo.pageSize = val
      this.getQuestionInfo()
    },
    // 分页插件的页数
    handleCurrentChange (val) {
      this.queryInfo.pageNo = val
      this.getQuestionInfo()
    },
    // 选择分类
    handleChange(){
      this.queryInfo.mt = this.selectedCategories[0]
      this.queryInfo.st = this.selectedCategories[1]
      this.getListInfo()
    },
    // 查询分类数据
    fetchData () {
      courseCategory.FindNodes().then((resp) => {
        if (resp.code === 200) {
          this.categoryOptions = resp.data
          console.log(this.categoryOptions)
        }
        this.$notify({
          title: 'Tips',
          message: resp.message,
          type: 'success',
          duration: 2000
        })
      })
    },    
    //导入图标前
    beforeUpload(){
      this.Uploadloading = true
    },
    // 导入图标后
    importFile(val){
      debugger
      console.log(val)
      this.Uploadloading = false
      this.categoryInfo.pic = val.data
      // 清除上传列表
      this.$refs.upload.clearFiles();
    },
  },
  computed: {
    // 监测头部信息的token变化
    headers () {
      const signHeaders = {
        'body-string': '',
        'query-string': '',
        'x-nonce': `${utils.getRandomId()}`,
        'x-timestamp': `${new Date().getTime()}`
      }
      return {
        ...signHeaders,
        authorization: localStorage.getItem('authorization') || '',
        sign: generateSign(JSON.stringify(signHeaders))
      }
    },
    // // 计算总分
    // sumTotalScore () {
    //   let score = 0
    //   this.updateExamQuestion.forEach(item => {
    //     score += parseInt(item.score)
    //   })
    //   this.categoryInfo.totalScore = score
    //   return score
    // },

  }
}
</script>

<style scoped lang="scss">
.el-container {
  width: 100%;
  height: 100%;
}

.el-container {
  animation: leftMoveIn .7s ease-in;
}

@keyframes leftMoveIn {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0%);
    opacity: 1;
  }
}

/deep/ .el-table thead {
  color: rgb(85, 85, 85) !important;
}

/*表格的头部样式*/
/deep/ .has-gutter tr th {
  background: rgb(242, 243, 244);
  color: rgb(85, 85, 85);
  font-weight: bold;
  line-height: 32px;
}

.el-table {
  box-shadow: 0 0 1px 1px gainsboro;
}




.upload-placeholder {
  width: 200px;
  height: 200px;
  border: 1px dashed #ccc;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #999;
}

.placeholder p {
  
  margin: 5px 0;
}
.preview-image {
  // width: 200px; /* 固定宽度 */
  // height: auto; /* 自动调整高度以保持原始宽高比 */
  /* 或者使用百分比 */
  width: 100%; 
  height: auto;
}
</style>
