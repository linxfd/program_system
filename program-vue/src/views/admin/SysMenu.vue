<template>
  <div>
    <div class="tools-div">
      <el-button type="success" size="small" @click="addShow">添 加</el-button>
    </div>
    <!--添 加的弹框  -->
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="30%">
      <el-form label-width="120px">
        <el-form-item label="菜单标题">
          <el-input v-model="sysMenu.topMenuName" />
        </el-form-item>
        <el-form-item label="路由名称">
          <el-input v-model="sysMenu.url" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="sysMenu.topIcon" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="sysMenu.sortValue" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="sysMenu.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveOrUpdate">提交</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
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
      <el-table-column prop="topMenuName" label="菜单标题" />
      <el-table-column prop="url" label="路由名称" />
      <el-table-column prop="topIcon" label="图标" />
      <el-table-column prop="sortValue" label="排序" />
      <el-table-column prop="status" label="状态" #default="scope">
        {{ scope.row.status == 1 ? '正常' : '停用' }}
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />

      <el-table-column label="操作" align="center" width="280" #default="scope">
        <el-button type="success" size="small" @click="addShow(scope.row)">
          添加下级节点
        </el-button>
        <el-button type="primary" size="small" @click="editShow(scope.row)">
          修改
        </el-button>
        <el-button type="danger" size="small" @click="remove(scope.row.id)">
          删除
        </el-button>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
//引入调用的方法
import { ref, onMounted } from 'vue'
import {
  FindNodes,
  SaveMenu,
  UpdateSysMenuById,
  RemoveSysMenuById,
} from '@/api/sysMenu'
import utils from '@/utils/utils'

// 定义表格数据模型
const list = ref([])

// 定义添加表单菜单表单相关数据模型
const dialogTitle = ref('添加')
const dialogVisible = ref(false)

//页面表单数据
const defaultForm = {
  id: '',
  parentId: 0,
  topMenuName: '',
  url: '',
  component: '',
  icon: '',
  sortValue: 1,
  status: 1,
}

// 表单响应式数据模型对象
const sysMenu = ref(defaultForm)

//=======================加载数据=========================
onMounted(() => {
  fetchData()
})

//=======================添加和修改功能====================
//进入添加
const addShow = row => {
  sysMenu.value = {}
  dialogVisible.value = true  
  if (!row.id) {
    dialogTitle.value = '添加'
  } else {
    dialogTitle.value = '添加下级节点'
    sysMenu.value.parentId = row.id
  }
}

//进入修改
const editShow = row => {
  sysMenu.value = { ...row }
  dialogVisible.value = true
}

//提交保存与修改
const saveOrUpdate = () => {
  //没有id，添加操作
  if (!sysMenu.value.id) {
    if (!sysMenu.value.parentId) {
      sysMenu.value.parentId = 0
    }
    saveData()
  } else {
    updateData()
  }
}

// 修改
const updateData = async () => {
  const code = await UpdateSysMenuById(sysMenu.value)
  if (code === 200) {
    // ElMessage.success('操作成功')
    
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
  dialogVisible.value = false

  fetchData()
}

// 新增
const saveData = async () => {
  await SaveMenu(sysMenu.value)
  dialogVisible.value = false
  this.$notify({
              title: 'Tips',
              message: '操作成功',
              type: 'success',
              duration: 2000
            })
  fetchData()
}

//=======================分页列表====================
const fetchData = async () => {
  const { code, data, message } = await FindNodes()

  console.log(data)
  list.value = data
}

//=======================删除功能====================
const remove = async id => {
  console.log('removeDataById:' + id)
  ElMessageBox.confirm('此操作将永久删除该记录, 是否继续?', 'Warning', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }
  
  ).then(async () => {
    const { code, message } = await RemoveSysMenuById(id)
    if (code === 200) {
      ElMessage.success('删除成功')
      fetchData()
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
