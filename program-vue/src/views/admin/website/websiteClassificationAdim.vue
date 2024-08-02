<template>
  <el-container>
    <el-header>
      <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent v-show="showSearch">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入分类名称" 
           @keyup.enter.native="handleQuery"
           @blur="handleQuery"
            clearable class="inp"/>
        </el-form-item>

        <el-form-item label="分类信息" prop="notes">
          <el-input v-model="queryParams.notes" placeholder="请输入分类信息" 
           @keyup.enter.native="handleQuery"
           @blur="handleQuery"
           clearable class="inp"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-plus"  @click="showAddDialog('添加')" >添加</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main>
      <el-select
        @change="selectChange"
        clearable
        v-if="ids.length !== 0"
        v-model="selected"
        :placeholder="'已选择' + ids.length + '项'"
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
      :data="response.data"
      :border="true"
      @selection-change="selects" 
      tooltip-effect="dark"
      style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="分类名称" align="center" prop="name" />
        <el-table-column label="分类注解" align="center" prop="notes" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope" >
            <el-button size="mini" type="text" icon="el-icon-edit" @click="showAddDialog('修改',scope.row)" >修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="del(scope.row)" >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页插件 -->
      <el-pagination 
        style="margin-top: 25px"
        v-if="response.total>0" 
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="response.total"
        />
    </el-main>

    <el-dialog
      :title="dialogName"
      :visible.sync="dialogVisible"
      width="30%"
      @close="resetAddForm"
      center
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入网站名称" clearable/>
        </el-form-item>
        <el-form-item label="分类注解" prop="notes">
          <el-input v-model="form.notes" placeholder="请输入网站注解" clearable/>
        </el-form-item>

      </el-form>
      <div class="mfooter">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="$layer.close(layerid)">取 消</el-button>
    </div>
    </el-dialog>
  </el-container>

</template>

<script>
import website from '@/api/website'

export default {
  name: "Website",
  data() {
    return {
      ids: [],
      form: {},
      showSearch:true,
      response: {},
      // 对话框是否显示
      dialogVisible:false,
      // 对话框标题
      dialogName:'添加',
      // 下拉框所选择的数据
      selected: '',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        notes: null,
        name: null,
      },
      optionInfo: [
        {
          label: '删除',
          desc: 'delete'
        }
      ],
      rules: {
        name: [
          { required: true, message: "网站名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      website.getClassificationList(this.queryParams).then((resp) => {
        if (resp.code == 200){
            this.response = resp.data;
        }
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    selects(rows) {
      this.ids = rows.map(item => item.id)
    },
    //提交表单
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id) {
              website.editClassification(this.form).then(resp => {
                  this.$notify({
                    title: 'Tips',
                    message: resp.message,
                    type: 'success',
                    duration: 2000
                  })
                  this.dialogVisible = false;
                  this.getList();
              });
          } else {
            website.addClassification(this.form).then(resp => {
                this.$notify({
                  title: 'Tips',
                  message: resp.message,
                  type: 'success',
                  duration: 2000
                })
                this.dialogVisible = false;
                this.getList();
              });
          }
        }
      });
    },
    // 点击添加按钮
    showAddDialog (vle,row) {
      if(row != null){
        this.form = {...row}
      }
      this.uerVisible = vle
      this.dialogVisible = true
    },
    InitialSizeandCurrentChange () {
      this.queryParams.pageNo = 1
      this.queryParams.pageSize = 10
    },
    // 表单信息重置
    resetAddForm () {
      // 清空表格数据
      this.form = {}
    },
    del(row) {
        this.$confirm('是否确认删除选中数据?', '警告', { type: 'warning' }).then(() => {
          website.removeClassification((row.id || this.ids)).then(response => {
            if (response.code == 200){
              this.$notify({
                    title: 'Tips',
                    message: '操作成功',
                    type: 'success',
                    duration: 2000
                  })
                  this.getList();
            }else{
              this.$notify({
                    title: 'Tips',
                    message: response.message,
                    type: 'error',
                    duration: 2000
                  })
            }
                
            });
        });
    },
    // 功能下拉框被选择
    selectChange (val) {
      let id = {}
      this.del(id)
      
    },
     // 分页插件的大小改变
    handleSizeChange (val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    // 分页插件的页数
    handleCurrentChange (val) {
      this.queryParams.pageNum = val
      this.getList()
    },
  }
};
</script>
