<template>
  <el-container>
    <el-header>
      <el-input
        v-model="queryInfo.name"
        @blur="usernameChange"
        placeholder="搜索登录名"
        prefix-icon="el-icon-search"
      />
      <el-input
        v-model="queryInfo.description"
        @blur="trueNameChange"
        placeholder="搜索姓名"
        prefix-icon="el-icon-search"
        style="margin-left: 5px"
      />
      <el-cascader  
        :options="categoryOptions"  
        :props="cascaderProps"  
        style="margin-left: 5px"
        placeholder="搜索分类"
        v-model="selectedCategories"  
        clearable  
        @change="handleChange"  
      />
      <el-button
        type="primary"
        style="margin-left: 5px"
        icon="el-icon-plus"
        @click="$router.push('/course/CourseBaseModel/0')"
      >
      <!-- /course/CourseBaseModel/0  id为0时，为添加 -->
        添加
      </el-button>
    </el-header>

    <el-main>
      <!--操作的下拉框-->
      <el-select
        @change="selectChange"
        clearable
        v-if="selectedInTable.length !== 0"
        v-model="selected"
        :placeholder="'已选择' + selectedInTable.length + '项'"
        style="margin-bottom: 25px;"
      >
        <el-option
          v-for="(item,index) in optionInfo"
          :key="index"
          :value="item.desc"
        >
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.desc }}</span>
        </el-option>
      </el-select>

      <el-table
        ref="multipleTable"
        highlight-current-row
        v-loading="loading"
        :border="true"
        :data="categoryInfo"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column label="课程名称" align="center" prop="name" />
        <el-table-column label="课程图片" align="center" prop="pic" > 
          <template slot-scope="scope">
              <img :src="scope.row.pic" alt="" style="height: 50px;">
          </template>
        </el-table-column>
        
        <el-table-column label="大分类" align="center"  >
          <template slot-scope="scope">
            {{ formattedMtName(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="小分类" align="center">
          <template slot-scope="scope">
            {{ formattedStName(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="课程介绍" align="center"  v-slot:default="scope" width="180px">
            <el-tooltip effect="dark" placement="top-start" >
              <template slot="content" >
                <div class="top-content">{{ scope.row.description }}</div>
              </template>
              <p class="p" style="max-width: 230px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                {{ scope.row.description }}
              </p>
        </el-tooltip>
        </el-table-column>
        <el-table-column label="发布状态" align="center" width="120">
          <template v-slot:default="scope">
            <div :class="['status-badge', {'status-badge-unpublished': scope.row.courseStatus === 1, 'status-badge-published': scope.row.courseStatus !== 1}]">
              {{ scope.row.courseStatus === 1 ? '未发布' : '已发布' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="积分模式" align="center" width="100">
          <template v-slot:default="scope">
            <div :class="['status-badge', {'status-badge-free': scope.row.charge === 1, 'status-badge-paid': scope.row.charge !== 1}]">
              {{ scope.row.charge === 1 ? '免费' : '积分' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="积分数量" align="center" prop="pointsNumber" >
           <template slot-scope="scope">
              {{scope.row.pointsNumber == 0 ? "-" : scope.row.pointsNumber}}
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="createPerson" />
        <el-table-column
          label="操作"
          align="center"
          width="280"
          #default="scope"
        >
          <el-button
            size="mini" type="text" icon="el-icon-edit"
            @click="editShow(scope.row)"
          >
            修改
          </el-button>
          <el-button
            size="mini" type="text" icon="el-icon-delete"
            @click="remove(scope.row)"
          >删除</el-button>
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

import category from '@/api/category'
import courseCategory from '@/api/courseCategory'

export default {
  name: 'CourseBase',
  data () {
    return {
      // 查询课程的参数
      queryInfo: {
        pageNo: 1,
        pageSize: 10
      },
      // 课程信息
      categoryInfo: [],
      // 下拉选择框的数据
      optionInfo: [
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
      statusInfo: [
        {
          name: '正常',
          status: '1'
        },
        {
          name: '禁用',
          status: '2'
        }
      ],
      // 下拉框所选择的数据
      selected: '',
      // 被选择的表格中的行数据
      selectedInTable: [],
      // 所有数据的条数
      total: 0,
      // 表格信息加载
      loading: true,
      selectedCategories: [],  
      categoryOptions: [],  
      //分类的配置
      cascaderProps: { 
        value: 'id',  // 使用 id 作为唯一标识符  
        label: 'label',  
        children: 'children',  
        disabled: 'isDisabled',  
        checkStrictly: true,
      },
    }
  },
  created () {
    // 查询第一次
    this.getListInfo()
    // 查询分类数据
    this.fetchData()

  },
  computed: {
    
  },
  methods: {
    //查询大分类的对应名称
    formattedMtName(row) {
      return this.categoryOptions.find(option => option.id == row.mt)?.name || '';
    },
    //查询小分类的对应名称
    formattedStName(row) {
      const mt = this.categoryOptions.find(option => option.id == row.mt)
      if(mt){
        return mt.children.find(option => option.id == row.st)?.name || '';
      }
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
    // 获取课程信息
    getListInfo () {
      category.getListInfo(this.queryInfo).then((resp) => {
        if (resp.code === 200) {
          this.categoryInfo = resp.data.data
          this.total = resp.data.total
          this.loading = false
        } else {
          this.$notify({
            title: 'Tips',
            message: '获取信息失败',
            type: 'error',
            duration: 2000
          })
        }
      })
    },
   
    trueNameChange(){
      this.InitialSizeandCurrentChange()
      this.getListInfo()
    },
    usernameChange(){
      this.InitialSizeandCurrentChange()
      this.getListInfo()
    },
    handleChange(){
      this.queryInfo.mt = this.selectedCategories[0]
      this.queryInfo.st = this.selectedCategories[1]
      this.getListInfo()
    },
    typeChange (val) {
      this.InitialSizeandCurrentChange()
      this.queryInfo.roleId = val
      this.getListInfo()
    },
    // 表格某一行被选中
    handleSelectionChange (val) {
      this.selectedInTable = val
    },
    // 功能下拉框被选择
    selectChange (val) {
      // 清空上一次的操作
      this.selected = ''
      // 表格中所选中的用户的id
      const userIds = []
      this.selectedInTable.map(item => {
        userIds.push(item.id)
      })
      if (val === 'on') { // 状态设置为正常
        category.handlerUser(1, { userIds: userIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            // 删除成功后,回调更新用户数据
            this.getListInfo()
            this.$notify({
              title: 'Tips',
              message: '操作成功',
              type: 'success',
              duration: 2000
            })
          } else {
            this.$notify({
              title: 'Tips',
              message: '操作失败',
              type: 'error',
              duration: 2000
            })
          }
        })
      } else if (val === 'off') { // 禁用用户
        category.handlerUser(2, { userIds: userIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            // 删除成功后,回调更新用户数据
            this.getListInfo()
            this.$notify({
              title: 'Tips',
              message: '操作成功',
              type: 'success',
              duration: 2000
            })
          } else {
            this.$notify({
              title: 'Tips',
              message: '操作失败',
              type: 'error',
              duration: 2000
            })
          }
        })
      } else if (val === 'delete') { // 删除用户
        category.handlerUser(3, { userIds: userIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            // 删除成功后,回调更新用户数据
            this.getListInfo()
            this.$notify({
              title: 'Tips',
              message: '操作成功',
              type: 'success',
              duration: 2000
            })
          } else {
            this.$notify({
              title: 'Tips',
              message: '操作失败',
              type: 'error',
              duration: 2000
            })
          }
        })
      }
    },
    // 分页插件的大小改变
    handleSizeChange (val) {
      this.queryInfo.pageSize = val
      this.getListInfo()
    },
    // 分页插件的页数
    handleCurrentChange (val) {
      this.queryInfo.pageNo = val
      this.getListInfo()
    },

    InitialSizeandCurrentChange () {
      this.queryInfo.pageNo = 1
      this.queryInfo.pageSize = 10
    },
    
    //= ===========删除====
    remove (val) {
      this.$confirm(`此操作将永久删除"${val.username}"用户, 是否继续?`, '注意', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        category.deleteUser(val.id).then((resp) => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
            this.getListInfo()
          } else {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'error',
              duration: 2000
            })
          }
        })
      })
    }
  }
}
</script>

<style scoped lang="scss">

.el-container {
  width: 100%;
  height: 100%;
}

.el-input {
  width: 200px;
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

.role {
  color: #606266;
}
.status-badge {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

// 
.status-badge-unpublished {
  background-color: #fffbe8;
  color: #f56c6c; /* Element UI默认的危险颜色 */
  border: 1px solid #fde2de;
}

.status-badge-published {
  background-color: #e1f3d8;
  color: #67c23a; /* Element UI默认的成功颜色 */
  border: 1px solid #c2e7b0;
}



.status-badge-free {
  background-color: #e6f7ff;
  color: #409EFF; /* Element UI默认的主要颜色 */
}

.status-badge-paid {
  background-color: #FFD700; /* VPI的金黄色背景 */
  color: #333; /* 深灰色文字，以提高可读性 */
}

  .top-content {
    white-space: pre-wrap; /* 允许文本换行 */
    word-break: break-word; /* 在长单词或URL上换行 */
    max-width: 230px;
    text-indent: 2em; /* 缩进2字符 */
  }
</style>
