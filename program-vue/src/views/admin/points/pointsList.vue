<template>
<el-container>
    <el-header>
    <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent v-show="showSearch">
      <el-form-item label="用户账户" prop="username">
        <el-input v-model="queryParams.username" placeholder="请输入用户账户"  @blur="handleQuery" clearable class="inp"/>
      </el-form-item>
      <el-form-item label="积分流水" prop="pointsFlow">
        <el-input v-model="queryParams.pointsFlow" placeholder="请输入积分流水"  @blur="handleQuery" clearable class="inp"/>
      </el-form-item>
      <el-form-item label="积分备注" prop="notes">
        <el-input v-model="queryParams.notes" placeholder="请输入积分备注"  @blur="handleQuery" clearable class="inp"/>
      </el-form-item>
          <el-button
        type="primary"
        style="margin-left: 5px"
        icon="el-icon-plus"
        @click="showAddDialog"
      >
        添加
      </el-button>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
        <el-button type="primary" icon="el-icon-plus" :disabled="ids.length > 0"  >新增</el-button>
        <el-button type="success" icon="el-icon-edit" :disabled="ids.length != 1"  >修改</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="ids.length == 0" @click="del" v-hasPermi="['work:points:remove']">删除{{ids.length>0?'('+ids.length+')':''}}</el-button>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->
    </el-header>

    <el-main>

      <el-table 
        :data="response" 
        border @selection-change="selects"
        style="width: 100%">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户id" align="center" prop="userId" />
        <el-table-column label="用户账户" align="center" prop="username" />
        <el-table-column label="积分流水" align="center" prop="pointsFlow" />
        <el-table-column label="积分备注" align="center" prop="notes" />
        <el-table-column  
          label="获取方式"  
          align="center"  
          prop="obtainMethod"  
          :formatter="formatObtainMethod"  
        />
        <el-table-column label="操作" align="center" #default="scope">

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
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
      </el-main>

      <!-- 添加或修改对话框 -->
      <el-dialog
        :title="visible"
        :visible.sync="addTableVisible"
        width="30%"
        @close="resetAddForm"
        center
      >
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="用户账户" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户账户" @blur="usernameChange" clearable/>
          </el-form-item>

          <el-form-item label="积分类型" prop="obtainMethod">  
            <el-select v-model="form.obtainMethod" placeholder="请选择积分类型">  
              <el-option  
                v-for="item in pointsTypes"  
                :key="item.value"  
                :label="item.label"  
                :value="item.value"  
              ></el-option>  
            </el-select>  
          </el-form-item> 

          <el-form-item label="用户id" prop="userId">
            
            <el-input v-model="form.userId" placeholder="请输入用户id" clearable/>
            <span style="color: #e61106;" v-if="isusername">注意该用户名可能不存在</span>
          </el-form-item>
          <el-form-item label="积分流水" prop="pointsFlow">
            <el-input-number v-model="form.pointsFlow" placeholder="请输入积分流水" clearable/>
          </el-form-item>
          <el-form-item label="积分备注" prop="notes">
            <el-input v-model="form.notes" placeholder="请输入积分备注" clearable/>
          </el-form-item>

        </el-form>
      <div class="mfooter">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="addTableVisible = false">取 消</el-button>
      </div>
      </el-dialog>
    </el-container>
</template>

<script>
import signIn from '@/api/signIn'
import user from '@/api/user'

export default {
  name: "Points",
  data() {
    return {
      ids: [],
      showSearch:true,
      response: [],
      total: 0,
      isusername: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: null,
        userId: "",
        pointsFlow: null,
        notes: null,
        obtainMethod: null,
        isDeleted: null,
        // orderByColumn: 'id',
      },
      // 弹窗名称
      visible: '添加积分日志',
      // 添加用户的对话框是否显示
      addTableVisible: false,
      form: {},
      pointsTypes: [ // 积分类型选项  
        { value: 1, label: '签到' },  
        { value: 2, label: '考试' },
        { value: 10, label: '其他' },  
      ],
      rules: {
        username: [
          { required: true, message: "用户账户不能为空", trigger: "blur" }
        ],
        pointsFlow: [
          { required: true, message: "积分流水不能为空", trigger: "blur" }
        ],
        obtainMethod: [
          { required: true, message: "获取方式不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {

    usernameChange(){
      const userInfo = {"username": this.form.username}
      user.getUserInfo(userInfo).then((resp)=>{
        if (resp.code === 200) {
          if(resp.data.data[0] != null){
            this.form.userId = resp.data.data[0].id
          }
          else{
          this.isusername = true
          }
        } 
      })
    },
    // 修改
    editShow (row) {
      this.visible = '修改用户'
      this.form = { ...row }
      this.addTableVisible = true
    },
    //============删除====
    remove (val) {
      this.$confirm(`此操作将永久删除该积分, 是否继续?`, '注意', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        signIn.remove(val.id).then((resp) => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
            this.getList()
          } 
        })
      })

         
    },
    formatObtainMethod(row, column, cellValue){
      // 假设 1 表示“签到”，2 表示“考试”，其他为“其他”  
      switch (cellValue) {
        case 1:  
          return '签到';  
        case 2:  
          return '考试';  
        default:  
          return '其他';  
      }  
    },
    // 分页插件的大小改变
    handleSizeChange (val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    // 分页插件的页数
    handleCurrentChange (val) {
      debugger
      console.log(val)
      this.queryParams.pageNum = val
      this.getList()
    },
    showAddDialog(){
      this.visible = '添加用户'
      this.addTableVisible = true
    },
    // 表单信息重置
    resetAddForm () {
      // 清空表格数据
      this.form = {
      }
      this.isusername = false
    },
    getList() {
      signIn.getList(this.queryParams).then(resp => {
        if (resp.code == 200){
            console.log(resp.data)
            this.response = resp.data.data;
            this.total = resp.data.total;
        }
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    selects(rows) {
      this.ids = rows.map(item => item.id)
    },
    // 添加/修改
    submitForm() {
      debugger
      if (this.form.id) {
        signIn.edit(this.form).then(resp => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
            this.getList()
          } 
          this.addTableVisible = false
        })
      }else{
        signIn.add(this.form).then(resp => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
            this.getList()
          } 
          this.addTableVisible = false
        })

      }
      
    }

  }
};
</script>

<style scoped lang="scss">
  
</style>
