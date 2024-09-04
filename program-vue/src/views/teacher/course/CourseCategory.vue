<template>
  <div>
    <div class="tools-div">
      <el-button
        type="success"
        size="small"
        @click="addShow"
      >
        添加顶级分类
      </el-button>
    </div>
    <!--添 加的弹框  -->
    <el-dialog
      :visible.sync="dialogVisible"
      :title="dialogTitle"
      width="30%"
    >
      <el-form label-width="120px">
        <el-form-item label="分类名称">
          <el-input v-model="courseCategory.name"  />
        </el-form-item>
        <el-form-item  >
            <span slot="label">
                分类标签
                <el-tooltip 
                    effect="dark"
                    content="分类标签默认和名称一样"
                    placement="top-end"
                    >
                <i class="el-icon-question"></i>
                </el-tooltip>
            </span>
          <el-input v-model="courseCategory.label" placeholder="分类标签默认和名称一样" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="courseCategory.sortValue" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="courseCategory.status">
            <el-radio :label="1">
              正常
            </el-radio>
            <el-radio :label="0">
              停用
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="saveOrUpdate"
          >
            提交
          </el-button>
          <el-button @click="dialogVisible = false">
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-table
      :data="list"
      style="width: 100%; margin-bottom: 20px"
      row-key="id"
      border
      default-expand-all
    >
      <el-table-column
        prop="name"
        label="分类名称"
      />
      <el-table-column
        prop="label"
        label="分类标签"
      />
      <el-table-column
        prop="sortValue"
        label="排序"
      />
      <el-table-column
        prop="status"
        label="状态"
        #default="scope"
      >
        {{ scope.row.status == 1 ? '正常' : '停用' }}
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
      />

      <el-table-column
        label="操作"
        align="center"
        width="280"
        #default="scope"
      >
        <el-button
          size="mini" type="text" icon="el-icon-edit-outline"
          @click="addShow(scope.row)"
        >
          添加下级节点
        </el-button>
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
  </div>
</template>

<script>

import courseCategory from '@/api/courseCategory'

export default {
  name: 'CourseCategory', // 请根据实际情况命名

  data () {
    return {
      list: [],
      dialogTitle: '添加',
      dialogVisible: false,
      courseCategory: {
        id: '',
        parentId: 0,
        name: '',
        sortValue: 1,
        status: 1
      }
    }
  },
  // Vue 实例完成挂载后立即调用
  mounted () {
    this.fetchData()
  },
  methods: {
    //= =======查看列表
    async fetchData () {
      courseCategory.FindNodes().then((resp) => {
        if (resp.code === 200) {
          this.list = resp.data
        }
        this.$notify({
          title: 'Tips',
          message: resp.message,
          type: 'success',
          duration: 2000
        })
      })
    },
    //= ======================添加和修改功能====================
    // 进入添加
    addShow (row) {
      this.courseCategory = {}
      this.dialogVisible = true
      if (!row.id) {
        this.dialogTitle = '添加'
      } else {
        this.dialogTitle = '添加下级节点'
        this.courseCategory.parentId = row.id
      }
    },
    // 进入修改
    editShow (row) {
      this.courseCategory = { ...row }
      this.dialogVisible = true
    },
    // 提交保存与修改
    saveOrUpdate () {
      if (!this.courseCategory.id) {
        if (!this.courseCategory.parentId) {
          this.courseCategory.parentId = 0
        }
        this.saveData()
      } else {
        this.updateData()
      }
    },
    // 修改
    updateData () {
      courseCategory.UpdateCourseCategoryById(this.courseCategory).then((resp) => {
        if (resp.code === 200) {
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
        } else {
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'error',
            duration: 2000
          })
        }
        this.dialogVisible = false
        this.fetchData()
      })
    },
    // 新增
    async saveData () {
      courseCategory.SaveCategory(this.courseCategory).then((resp) => {
        if (resp.code === 200) {
          this.dialogVisible = false
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
          this.fetchData()
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
    //= ======================删除功能====================
    async remove (row) {
      this.$confirm(`此操作将永久删除"${row.name}"该记录, 是否继续?`, '注意', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        courseCategory.RemoveCourseCategoryById(row.id).then((resp) => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
            this.fetchData()
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

<style scoped>
.search-div {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}
.tools-div {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 3px;
  background-color: #fff;
}
</style>
