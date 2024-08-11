<template>
  <el-container>
    <el-header>
      <el-form :model="queryParams" ref="queryForm" :inline="true" @submit.native.prevent v-show="showSearch">
        <el-form-item label="网站名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入网站名称" 
           @keyup.enter.native="handleQuery"
           @blur="handleQuery"
            clearable class="inp"/>
        </el-form-item>
        <el-form-item label="网址" prop="url">
          <el-input v-model="queryParams.url" placeholder="请输入网址" 
           @keyup.enter.native="handleQuery"
           @blur="handleQuery"
           clearable class="inp"/>
        </el-form-item>
        分类
        <el-select
          label="网站sd"
          @change="typeChange"
          clearable
          v-model="queryParams.classificationId"
          placeholder="请选择网站分类"
          style="margin-left: 5px"
        >
          <el-option
            v-for="item in classificationList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>

        <el-form-item label="网站注解" prop="notes" style="margin-left: 5px">
          <el-input v-model="queryParams.notes" placeholder="请输入网站注解" 
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
        <el-table-column label="网址名称" align="center" prop="name" />
        <el-table-column label="网址" align="center" prop="url" />
        <el-table-column label="网站图标" align="center" prop="icon" >
          <template slot-scope="scope">
            <img :src="scope.row.icon" alt="" style="width: 50px;height: 50px;">
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="网站分类"
          >
            <template slot-scope="scope">
              <span >{{getClassification(scope.row.classificationId)}}</span>
            </template>
          </el-table-column>
        <el-table-column label="权值" align="center" prop="sortValue" />
        <el-table-column label="网站注解" align="center" prop="notes" />
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

    <!-- 添加修改对话框  -->
    <el-dialog
      :title="dialogName"
      :visible.sync="dialogVisible"
      width="30%"
      @close="resetAddForm"
      center
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="网址名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入网站名称" clearable/>
        </el-form-item>
        <el-form-item label="网址" prop="url">
          <el-input v-model="form.url" placeholder="请输入网址"  @input="handleIcon" clearable/>
        </el-form-item>

        <el-form-item
          label="网站分类"
          label-width="120px"
          prop="classificationId"
        >
          <el-select
            v-model="form.classificationId"
            placeholder="请选择用户权限"
          >
            <el-option
              v-for="item in classificationList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="网站图标" prop="icon">
          <el-input v-model="form.icon" placeholder="默认为/favicon.ico" clearable/>
          <div style="display: flex;">
            <img :src="form.icon" alt="" style="width: 50px;height: 50px; margin-right: 15%;">
            <el-upload
                  ref="upload"
                  :action="uploadImageUrl + '/teacher/uploadQuestionImage'"
                  name="file"
                  :headers="headers"
                  :before-upload="beforeUpload"
                  :on-success="importFile"
                  v-loading="loading"
                >
                <el-button
                  size="small"
                  type="primary"
                >
                  换图标
                </el-button>
              </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="权值" prop="notes">
          <el-input-number v-model="form.sortValue"  clearable/>
        </el-form-item>

        <el-form-item label="网站注解" prop="notes">
          <el-input v-model="form.notes" placeholder="请输入网站注解" clearable/>
        </el-form-item>

      </el-form>
      <div class="mfooter">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </div>
    </el-dialog>
  </el-container>

</template>

<script>
import website from '@/api/website'
import utils from '@/utils/utils'
import { generateSign } from '@/utils/sign'
import apis from '@/api/api'

export default {
  name: "Website",
  data() {
    return {
      uploadImageUrl: process.env.VUE_APP_UPLOAD_IMAGE_URL,
      ids: [],
      form: {
        name: '',
        url: '',
        icon: '',
        sortValue: '',
        notes: '',
        classificationId: ''
      },
      showSearch:true,
      response: {},
      // 对话框是否显示
      dialogVisible:false,
      // 对话框标题
      dialogName:'添加',
      // 下拉框所选择的数据
      selected: '',
      // 网站分类数据
      classificationList:{},
      // 数据预加载
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        notes: null,
        name: null,
        isDeleted: null,
        classificationId: null,
        orderByColumn: 'id',
        isAsc: 'desc'
      },
      optionInfo: [
        {
          label: '删除',
          desc: 'delete'
        }
      ],
      rules: {
        url: [
          { required: true, message: "网址不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "网站名称不能为空", trigger: "blur" }
        ],
        classificationId: [
          { required: true, message: "网站分类不能为空", trigger: "blur" }
        ],
      }
    };
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
    }
  },
  created() {
    this.getList();
    this.getClassificationList();
  },
  methods: {
    getList() {
      website.getList(this.queryParams).then((resp) => {
        if (resp.code == 200){
            this.response = resp.data;
        }
      });
    },
     getClassificationList() {
      const queryParams = {
              pageNum: 1,
              pageSize: 999,
            }
      website.getClassificationList(queryParams).then((resp) => {
        if (resp.code == 200){
            this.classificationList = resp.data.data;
        }
      });
    },
    getClassification(roleId){
      const classObj = this.classificationList.find(classif => classif.id === roleId)
      return classObj ? classObj.name : '未知分类'
     },
    typeChange (val) {
      this.InitialSizeandCurrentChange()
      this.queryParams.classificationId = val
      this.getList()
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
    handleIcon(){
      this.form.icon = this.form.url+'/favicon.ico'
      const urlcom =  encodeURIComponent(this.form.url)
       console.log(this.form.url)
      console.log(urlcom)
      debugger
      apis.postHttps(urlcom).then((response)=>{
        debugger
        if (response.code === 200) {
          let html = response.data.contents;
          let parser = new DOMParser();
          console.log(html)
          let doc = parser.parseFromString(html, 'text/html');
          console.log(doc.querySelector('title').textContent)
          this.form.name = doc.querySelector('title').textContent;
        }
      })
    },
    //提交表单
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id) {
              website.edit(this.form).then(resp => {
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
            website.add(this.form).then(resp => {
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
      this.dialogName = vle
      this.dialogVisible = true
    },
    InitialSizeandCurrentChange () {
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
    },
    // 表单信息重置
    resetAddForm () {
      // 清空表格数据
      this.form = {}
    },
    del(row) {
        this.$confirm('是否确认删除选中数据?', '警告', { type: 'warning' }).then(() => {
        website.remove((row.id || this.ids)).then(response => {
                this.$notify({
                  title: 'Tips',
                  message: '操作成功',
                  type: 'success',
                  duration: 2000
                })
                this.getList();
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
    //导入图标前
    beforeUpload(){
      this.loading = true
    },
    // 导入图标后
    importFile(val){
      
      this.form.icon = val.data
      this.loading = false
      // 清除上传列表
      this.$refs.upload.clearFiles();

    },
  }
};
</script>
