<template>
  <el-container v-loading="loadingDownloadExcel">
    <el-header height="220">
      <el-select
        @change="typeChange"
        clearable
        v-model="queryInfo.examType"
        placeholder="请选择考试类型"
      >
        <el-option
          v-for="item in examType"
          :key="item.type"
          :label="item.info"
          :value="item.type"
        />
      </el-select>

      <el-date-picker
        style="margin-left: 5px"
        v-model="queryInfo.startTime"
        @change="getExamInfoChange"
        type="date"
        placeholder="考试开始时间"
      />

      <el-date-picker
        style="margin-left: 5px"
        @change="getExamInfoChange"
        v-model="queryInfo.endTime"
        type="date"
        placeholder="考试结束时间"
      />

      <el-input
        v-model="queryInfo.examName"
        placeholder="考试名称"
        @blur="getExamInfoChange"
        style="margin-left: 5px;width: 220px"
        prefix-icon="el-icon-search"
      />
      <br>
      <el-button
        type="primary"
        icon="el-icon-plus"
        style="margin-top: 10px"
        @click="$router.push('/addExam')"
      >
        添加
      </el-button>
    </el-header>

    <el-main>
      <!--操作的下拉框-->
      <el-select
        @change="operationChange"
        clearable
        v-if="selectionTable.length !== 0"
        v-model="operation"
        :placeholder="'已选择' + selectionTable.length + '项'"
        style="margin-bottom: 25px;"
      >
        <el-option
          v-for="(item,index) in operationInfo"
          :key="index"
          :value="item.desc"
        >
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.desc }}</span>
        </el-option>
      </el-select>

      <el-table
        ref="questionTable"
        highlight-current-row
        v-loading="loading"
        :border="true"
        :data="examInfo"
        tooltip-effect="dark"
        style="width: 100%;"
        @selection-change="handleTableSectionChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="55"
        />

        <el-table-column
          align="center"
          label="考试名称"
        >
          <template slot-scope="scope">
            <span
              style="cursor:pointer;color: rgb(24,144,255)"
              @click="$router.push('/updateExam/'+ scope.row.examId)"
            >{{
              scope.row.examName
            }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="考试类型"
        >
          <template slot-scope="scope">
            {{ scope.row.type === 1 ? '完全公开' : '需要密码' }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="考试时间"
        >
          <template slot-scope="scope">
            {{
              scope.row.startTime !== 'null' && scope.row.endTime !== 'null' ?
                scope.row.startTime + ' ~' + scope.row.endTime : '不限时'
            }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="totalScore"
          label="考试总分"
        />

        <el-table-column
          align="center"
          prop="passScore"
          label="及格分数"
        />

        <el-table-column
          align="center"
          prop="duration"
          label="考试时长(分)"
        />

        <el-table-column
          align="center"
          label="状态"
        >
          <template #default="scope">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="操作"
        >
          <template #default="scope">
            <el-button
              type="primary"
              icon="el-icon-download"
              size="small"
              @click="downloadStudentExamScore(scope.row.examId)"
            >
              成绩导出
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--分页-->
      <el-pagination
        style="margin-top: 25px"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNo"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </el-main>
  </el-container>
</template>

<script>
import exam from '@/api/exam'
import { json2excel } from '@/utils/setMethods'
import { dateToStr } from "@/utils/DateUtil"

export default {
  name: 'ExamManage',
  data () {
    return {
      queryInfo: {
        examType: null,
        startTime: null,
        endTime: null,
        examName: null,
        pageNo: 0,
        pageSize: 10
      },
      // 表格是否在加载
      loading: true,
      // 考试类型信息
      examType: [
        {
          info: '公开考试',
          type: 1
        },
        {
          info: '需要密码',
          type: 2
        }
      ],
      // 被选择的考试的行
      selectionTable: [],
      // 表格多行选中的操作信息
      operation: '',
      // 支持操作的信息
      operationInfo: [
        {
          label: '启用',
          desc: 'on'
        },
        {
          label: '禁用',
          desc: 'off'
        },
        {
          label: '删除',
          desc: 'delete'
        }
      ],
      // 考试信息
      examInfo: [],
      // 查询到的考试总数
      total: 0,
      loadingDownloadExcel: false
    }
  },
  created () {
    this.getExamInfo()
  },
  methods: {
    // 考试类型搜索
    typeChange (val) {
      this.InitialSizeandCurrentChange()
      this.queryInfo.examType = val
      this.getExamInfo()
    },
    getExamInfoChange(){
      this.InitialSizeandCurrentChange()
      this.getExamInfo()
    },
    // 操作多行表格信息
    operationChange (val) {
      const examIds = []
      this.selectionTable.forEach(item => {
        examIds.push(item.examId)
      })
      if (val === 'on') {
        exam.operationExam(1, { ids: examIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            this.getExamInfo()
            this.$notify({
              type: 'success',
              title: 'Tips',
              message: '操作成功',
              duration: 2000
            })
          }
        })
      } else if (val === 'off') {
        exam.operationExam(2, { ids: examIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            this.getExamInfo()
            this.$notify({
              type: 'success',
              title: 'Tips',
              message: '操作成功',
              duration: 2000
            })
          }
        })
      } else if (val === 'delete') {
        exam.operationExam(3, { ids: examIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            this.getExamInfo()
            this.$notify({
              type: 'success',
              title: 'Tips',
              message: '操作成功',
              duration: 2000
            })
          }
        })
      }
    },
    // 查询考试信息
    getExamInfo () {
      const roleId = window.localStorage.getItem('roleId')
      // 如果是老师，则只查询自己的题库，管理员可以查看全部
      if(roleId == 2){
        this.queryInfo.createPerson = window.localStorage.getItem('username')
      }
      exam.getExamInfo(this.queryInfo).then((resp) => {
        if (resp.code === 200) {
          resp.data.data.forEach(item => {
            item.startTime = String(item.startTime).substring(0, 10)
            item.endTime = String(item.endTime).substring(0, 10)
          })
          this.examInfo = resp.data.data
          this.total = resp.data.total
          this.loading = false
        }
      })
    },
    // 处理表格被选中
    handleTableSectionChange (val) {
      this.selectionTable = val
    },
    // 分页页面大小改变
    handleSizeChange (val) {
      this.queryInfo.pageSize = val
      this.getExamInfo()
    },
    // 分页插件的页数
    handleCurrentChange (val) {
      this.queryInfo.pageNo = val
      this.getExamInfo()
    },
    InitialSizeandCurrentChange () {
      this.queryInfo.pageNo = 1
      this.queryInfo.pageSize = 10
    },
    downloadStudentExamScore (examId) {
      this.loadingDownloadExcel = true
      exam.exportStudentExamRecordToExcel(examId).then((res) => {
        let binaryData = {}
        binaryData = [...res.data]
        let sheetName = dateToStr("yyyyMMdd-HHmmss", new Date) + '成绩汇总'
        var excelDatas = []
        let str = {
            tHeader: ['考试名称', '学生名称', '客观成绩', '主观成绩','总得分','考试时间'],
            filterVal: ["examName", "studentName","objectiveScore", "subjectiveScore", 'totalScore','examTime'],
            tableDatas: binaryData,
            sheetName: ""
          }
          excelDatas.push(str);
          json2excel(excelDatas, sheetName, true, "xlsx")
          this.loadingDownloadExcel = false
        // binaryData.push(res)
        // // 获取blob链接
        // this.excelUrl = window.URL.createObjectURL(new Blob(binaryData, { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8' }))
        // // 创建完毕,动画结束
        // this.loadingDownloadExcel = false
        // const a = document.createElement('a')
        // a.href = this.excelUrl
        // a.download = '成绩汇总.xlsx'
        // a.click()
        // window.URL.revokeObjectURL(this.excelUrl)

      })
    }
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/css/teacher/examManage";
</style>
