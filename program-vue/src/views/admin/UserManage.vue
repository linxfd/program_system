<template>
  <el-container>
    <el-header>
      <el-input
        v-model="queryInfo.username"
        @blur="usernameChange"
        placeholder="搜索登录名"
        prefix-icon="el-icon-search"
      />
      <el-input
        v-model="queryInfo.trueName"
        @blur="trueNameChange"
        placeholder="搜索姓名"
        prefix-icon="el-icon-search"
        style="margin-left: 5px"
      />
      <el-select
        @change="typeChange"
        clearable
        v-model="queryInfo.roleId"
        placeholder="请选择角色类型"
        style="margin-left: 5px"
      >
        <el-option
          v-for="item in roleName"
          :key="item.id"
          :label="item.roleName"
          :value="item.id"
        />
      </el-select>
      <el-button
        type="primary"
        style="margin-left: 5px"
        icon="el-icon-plus"
        @click="showAddDialog"
      >
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
        :data="userInfo"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          align="center"
          type="selection"
          width="55"
        />

        <el-table-column
          align="center"
          prop="username"
          label="用户名"
        />

        <el-table-column
          align="center"
          prop="trueName"
          label="姓名"
        />

        <el-table-column
          align="center"
          label="角色"
        >
          <template slot-scope="scope">
            <span class="role">{{ getRoleName(scope.row.roleId) }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="phone"
          label="手机号"
        />

        <el-table-column
          align="center"
          label="创建时间"
        >
          <template slot-scope="scope">
            {{ scope.row.createTime }}
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="状态"
        >
          <template slot-scope="scope">
            {{ scope.row.status === 1 ? '正常' : '禁用' }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="280"
          #default="scope"
        >
          <el-button
            type="primary"
            size="small"
            @click="editShow(scope.row)"
          >
            修改
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="small"
            @click="remove(scope.row)"
          />
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
    <!-- 添加或修改对话框 -->
    <el-dialog
      :title="uerVisible"
      :visible.sync="addTableVisible"
      width="30%"
      @close="resetAddForm"
      center
    >
      <el-form
        :model="addForm"
        :rules="currentRules"
        ref="addForm"
      >
        <el-form-item
          label="用户名"
          label-width="120px"
          prop="username"
        >
          <el-input v-model="addForm.username" />
        </el-form-item>

        <el-form-item
          label="密码"
          label-width="120px"
          prop="password"
        >
          <el-input
            v-if="!addForm.id"
            v-model="addForm.password"
            type="password"
            show-password
          />
          <el-input
            v-else
            v-model="addForm.password"
            type="password"
            show-password
            placeholder="不更改请留空"
          />
        </el-form-item>

        <el-form-item
          label="角色"
          label-width="120px"
          prop="roleId"
        >
          <el-select
            v-model="addForm.roleId"
            placeholder="请选择用户权限"
          >
            <el-option
              v-for="item in roleName"
              :key="item.id"
              :label="item.roleName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          label="真实姓名"
          label-width="120px"
          prop="trueName"
        >
          <el-input v-model="addForm.trueName" />
        </el-form-item>

        <el-form-item
          label="手机号"
          label-width="120px"
          prop="phone"
        >
          <el-input
            v-model="addForm.phone"
            placeholder="请输入绑定手机号"
          />
        </el-form-item>

        <el-form-item
          label="状态"
          label-width="120px"
          prop="roleId"
          v-if="addForm.id"
        >
          <el-select v-model="addForm.status">
            <el-option
              v-for="item in statusInfo"
              :key="item.status"
              :label="item.name"
              :value="item.status"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="addTableVisible = false">
          取 消
        </el-button>
        <el-button
          type="primary"
          @click="addUser"
        >
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import user from '@/api/user'
import utils from '@/utils/utils'
import auth from '@/api/auth'
import role from '@/api/role'

export default {
  name: 'UserManage',
  data () {
    // 自定义用户名校验规则
    const validateUsername = (rule, value, callback) => {
      if (this.addForm.id) {
        // 修改时
        auth.editUsername(this.addForm).then((resp) => {
          if (resp.data) {
            callback()
          } else {
            callback(new Error('用户名已存在'))
          }
        })
      } else {
        // 新增时
        auth.checkUsername(this.addForm.username).then((resp) => {
          if (resp.data) {
            callback()
          } else {
            callback(new Error('用户名已存在'))
          }
        })
      }
    }
    const isPhoneNumberValid = (rule, value, callback) => {
      const phoneRegex = /^1[3-9]\d{9}$/
      if (value === '' || value == '' || value == null) {
        callback()
      } else if (!phoneRegex.test(value)) {
        callback(new Error('请输入正确的手机号!'))
      } else {
        auth.checkUserPhone(value).then((resp) => {
          if (resp.data) {
            callback('ssa')
          } else {
            callback(new Error('手机号已存在'))
          }
        })
      }
    }
    const iseditPhoneNumber = (rule, value, callback) => {
      const phoneRegex = /^1[3-9]\d{9}$/
      console.log(value)
      if (value === '' || value == '' || value == null) {
        callback()
      } else if (!phoneRegex.test(value)) {
        callback(new Error('请输入正确的手机号!'))
      } else {
        auth.checkeditUserPhone(this.addForm).then((resp) => {
          if (resp.data) {
            callback()
          } else {
            callback(new Error('手机号已存在'))
          }
        })
      }
    }
    return {
      // 查询用户的参数
      queryInfo: {
        loginName: '',
        trueName: '',
        pageNo: 1,
        pageSize: 10
      },
      sysUser: [],
      // 用户信息
      userInfo: [],
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
      //
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
      // 所有用户的条数
      total: 0,
      // 添加用户的对话框是否显示
      addTableVisible: false,
      // 添加用户的表单信息
      addForm: {
        id: '',
        username: '',
        password: '',
        roleId: '',
        trueName: '',
        phone: '',
        status: ''
      },
      // 添加用户表单的验证规则
      addFormRules: {
        username: [
          {
            required: true,
            message: '请输入登录用户名',
            trigger: 'blur'
          },
          {
            validator: validateUsername,
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
          {
            min: 5,
            message: '密码必须5位以上',
            trigger: 'blur'
          }
        ],
        roleId: [
          {
            required: true,
            message: '请选择用户权限',
            trigger: 'blur'
          }
        ],
        phone: [
          {
            validator: isPhoneNumberValid,
            trigger: 'blur'
          }
        ]
      },
      // 修改用户表单的验证规则
      editFormRules: {
        username: [
          {
            required: true,
            message: '请输入登录用户名',
            trigger: 'blur'
          },
          {
            validator: validateUsername,
            trigger: 'blur'
          }
        ],
        password: [
          {
            min: 5,
            message: '密码必须5位以上',
            trigger: 'blur'
          }
        ],
        roleId: [
          {
            required: true,
            message: '请选择用户权限',
            trigger: 'blur'
          }
        ],
        phone: [
          {
            validator: iseditPhoneNumber,
            trigger: 'blur'
          }
        ]
      },
      // 表格信息加载
      loading: true,
      // 角色类型
      roleName: [],
      // 弹窗名称
      uerVisible: '添加用户'
    }
  },
  created () {
    // 查询第一次
    this.getUserInfo()
    // 获得角色名称列表
    this.getroleNames()
  },
  computed: {
    currentRules () {
      // 如果 addForm.id 存在，则使用 editFormRules，否则使用 addFormRules
      return this.addForm.id ? this.editFormRules : this.addFormRules
    }
  },
  methods: {
    // 获取用户信息
    getUserInfo () {
      user.getUserInfo(this.queryInfo).then((resp) => {
        if (resp.code === 200) {
          this.userInfo = resp.data.data
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
    // 获得角色名称列表
    getroleNames () {
      role.getRoleInfo().then((resp) => {
        if (resp.code === 200) {
          this.roleName = resp.data
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
        }
      })
    },
    getRoleName (roleId) {
      const roleNameObj = this.roleName.find(role => role.id === roleId)
      return roleNameObj ? roleNameObj.roleName : '未知角色'
    },
    trueNameChange(){
      this.InitialSizeandCurrentChange()
      this.getUserInfo()
    },
    usernameChange(){
      this.InitialSizeandCurrentChange()
      this.getUserInfo()
    },
    typeChange (val) {
      this.InitialSizeandCurrentChange()
      this.queryInfo.roleId = val
      this.getUserInfo()
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
        user.handlerUser(1, { userIds: userIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            // 删除成功后,回调更新用户数据
            this.getUserInfo()
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
        user.handlerUser(2, { userIds: userIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            // 删除成功后,回调更新用户数据
            this.getUserInfo()
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
        user.handlerUser(3, { userIds: userIds.join(',') }).then((resp) => {
          if (resp.code === 200) {
            // 删除成功后,回调更新用户数据
            this.getUserInfo()
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
      this.getUserInfo()
    },
    // 分页插件的页数
    handleCurrentChange (val) {
      this.queryInfo.pageNo = val
      this.getUserInfo()
    },
    // 点击添加按钮
    showAddDialog () {
      // this.addForm = {}
      this.uerVisible = '添加用户'
      this.addTableVisible = true
    },
    InitialSizeandCurrentChange () {
      this.queryInfo.pageNo = 1
      this.queryInfo.pageSize = 10
    },
    // 添加或更新用户
    addUser () {
      if (this.addForm.id) {
        user.updateUser(this.addForm).then((resp) => {
          if (resp.code === 200) {
            this.getUserInfo()
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
          this.addTableVisible = false
        })
      } else {
        utils.validFormAndInvoke(this.$refs.addForm, () => {
          user.addUser(this.addForm).then((resp) => {
            if (resp.code === 200) {
              this.getUserInfo()
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
            this.addTableVisible = false
          })
        }, '请检查您所填写的信息是否有误')
      }
    },
    // 修改
    editShow (row) {
      this.uerVisible = '修改用户'
      this.addForm = { ...row }
      this.addTableVisible = true
    },
    // 表单信息重置
    resetAddForm () {
      // 清空表格数据
      this.addForm = {
      }
    },
    //= ===========删除====
    remove (val) {
      this.$confirm(`此操作将永久删除"${val.username}"用户, 是否继续?`, '注意', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        user.deleteUser(val.id).then((resp) => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
            this.getUserInfo()
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
@import "../../assets/css/admin/userManage";
</style>
