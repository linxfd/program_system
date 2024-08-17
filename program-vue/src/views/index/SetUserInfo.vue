<template>
   <el-container class="el-container">
    <div class="main">
        <div class="from-login-left">
                <div class="sys-name2">个人信息</div>
          <div class="sys-name1">您在本网站已经有: {{ currentUserInfo.todate }}天</div>

        </div>
        <div class="from-login-right">
          <div class="user-info">
              <div class="title" >
                <p>{{ currentUserInfo.roleName }}</p>
              </div>

            <div class="row">
              <p class="info-item">账户: {{ currentUserInfo.username }}</p>
              <el-button plain class="modify-btn" @click="updateUser">修改账户昵称</el-button>
            </div>
            <hr>
            <div class="row">
               <p class="info-item">昵称: {{ currentUserInfo.trueName }}</p>
            </div>
            <hr>
            <div class="row">
               <p class="info-item">密码: ******</p>
               <el-button plain class="modify-btn" @click="updatePassword">&#8195;修改密码&#8195;</el-button>
            </div>
            <hr>
            <div class="row">
              <p class="info-item">手机号: {{ currentUserInfo.phone }}</p>
              <el-button plain class="modify-btn" @click="updatePhone">&nbsp;&nbsp;修改手机号&nbsp;&nbsp;</el-button>
            </div>
            <hr>

            <el-button
              class="return-btn"
              type="primary"
              icon="el-icon-position"
              @click="toDashboardPage" round>返回</el-button>
          </div>
        </div>
    </div>

    <el-dialog
        title="更新用户信息"
        center
        :visible.sync="updateCurrentUserDialog"
      >
        <el-form
          :model="currentUserInfo"
          :rules="updateUserFormRules"
          ref="updateUserForm"
        >
          <el-form-item 
            label="用户名"
            prop="username"
          >
            <el-input
              v-model="currentUserInfo.username"
            />
          </el-form-item>

          <el-form-item
            label="真实姓名"
            prop="trueName"
          >
            <el-input v-model="currentUserInfo.trueName" />
          </el-form-item>
        </el-form>

        <div
          slot="footer"
          class="dialog-footer"
        >
          <el-button @click="updateCurrentUserDialog = false">
            取 消
          </el-button>
          <el-button
            type="primary"
            @click="updateCurrentUser()"
          >
            确 定
          </el-button>
        </div>
      </el-dialog>

    <el-dialog
        title="换绑手机号"
        center
        :visible.sync="updateCurrentPhoneDialog"
      >
        <el-form
          :model="currentUserInfo"
          :rules="updatePhoneFormRules"
          ref="currentUserInfo"
        >
          <el-form-item label="用户名">
            <el-input
              v-model="currentUserInfo.username"
              disabled
            />
          </el-form-item>

          <el-form-item  label="手机号" prop="phone">
            <el-input
              
              prefix-icon="el-icon-phone"
              v-model="currentUserInfo.phone"
              placeholder="手机号"
            />
            </el-form-item>

            <el-form-item prop="code">
              <div style="display: flex">
                <el-input
                  class="code"
                  style="margin: 10px 12px 15px 1px"
                  prefix-icon="el-icon-chat-line-round"
                  v-model="currentUserInfo.codePhone"
                  placeholder="手机验证码"
                />
                <button class="zbutton" @click="getPhoneCode">获取验证码</button>
              </div>
            </el-form-item>
          <div
            class="dialog-footer"
          >
            <el-button @click="updateCurrentPhoneDialog = false">
              取 消
            </el-button>
            <el-button
              type="primary"
              @click="updateCurrentPhone"
            >
              确 定
            </el-button>
          </div>
        </el-form>

      </el-dialog>
      <el-dialog
        title="改密码"
        center
        :visible.sync="updatePasswordDialog"
      >
      <el-tabs v-model="activeName" @tab-click="handleClick" >
        <el-tab-pane label="根据原密码修改"  name="account" >
            <el-form
              :model="currentUserInfo"
              :rules="updatePasswordFormRules"
              ref="currentUserInfo"
            >
            <el-form-item  
                label="旧密码"
                prop="oldPassword"
              >
                <el-input
                  v-model="currentUserInfo.oldPassword"
                  placeholder="不更改请留空"
                />
              </el-form-item>

              <el-form-item  
                label="密码"
                prop="password"
              >
                <el-input
                  v-model="currentUserInfo.password"
                  placeholder="不更改请留空"
                />
              </el-form-item>
              
              <el-form-item
                label="再次输入密码"
                prop="passwordfun"
              >
                <el-input
                  v-model="currentUserInfo.passwordfun"
                  placeholder="不更改请留空"
                />
              </el-form-item>
              <div
            class="dialog-footer"
          >
            <el-button @click="updatePasswordDialog = false">
              取 消
            </el-button>
            <el-button
              type="primary"
              @click="updateCurrentUser('password')"
            >
              确 定
            </el-button>
          </div>
        </el-form>
        </el-tab-pane>
        <el-tab-pane label="根据手机号修改" name="phone">

          <el-form
            :model="currentUserInfo"
            :rules="updatePhoneFormRules"
            ref="currentUserInfo"
          >
          <el-form-item  label="手机号" prop="phone">
            <el-input
              disabled
              prefix-icon="el-icon-phone"
              v-model="currentUserInfo.phone"
              placeholder="手机号"
            />
            </el-form-item>

            <el-form-item prop="codePhone">
              <div style="display: flex">
                <el-input
                  style="margin: 10px 12px 15px 1px"
                  prefix-icon="el-icon-chat-line-round"
                  v-model="currentUserInfo.codePhone"
                  placeholder="手机验证码"
                />
                <button class="zbutton" @click="getPhoneCode">获取验证码</button>
              </div>
            </el-form-item>
            <el-form-item  
                label="密码"
                prop="password"
              >
                <el-input
                  v-model="currentUserInfo.password"
                  placeholder="不更改请留空"
                />
              </el-form-item>
              
              <el-form-item
                label="再次输入密码"
                prop="passwordfun"
              >
                <el-input
                  v-model="currentUserInfo.passwordfun"
                  placeholder="不更改请留空"
                />
              </el-form-item>
          <div
            class="dialog-footer"
          >
            <el-button @click="updatePasswordDialog = false">
              取 消
            </el-button>
            <el-button
              type="primary"
              @click="updateCurrentUser('phoneCode')"
            >
              确 定
            </el-button>
          </div>
        </el-form>
        </el-tab-pane>
      </el-tabs>
      </el-dialog>
  </el-container>

</template>

<script>
import user from '@/api/user'
import auth from '@/api/auth'
import router from '@/router/index'

export default {
  name: 'SetUserInfo',
  components: {},
  data() {
    // 自定义用户名校验规则
    const validateUsername = (rule, value, callback) => {
      // 修改时
      auth.editUsername(this.currentUserInfo).then((resp) => {
        if (resp.data) {
          callback()
        } else {
          callback(new Error('用户名已存在'))
        }
      })
    }
    const validatePassword = (rule, value, callback) => {
      debugger
      if (value === ''||this.currentUserInfo.oldPassword === ''||this.currentUserInfo.password === ''||this.currentUserInfo.passwordfun === '') {
        callback()
      } else if (value != '' && value.length < 5) {
        callback(new Error('新密码少于5位数!'))
      } else {
        callback()
      }
    }
    const validatePasswordfun = (rule, value, callback) => {
      if (value === ''||this.currentUserInfo.oldPassword === ''||this.currentUserInfo.password === ''||this.currentUserInfo.passwordfun === '') {
        callback()
      } else if (value != '' && value.length < 5) {
        callback(new Error('新密码少于5位数!'))
      }  else if(value != this.currentUserInfo.password){
        callback(new Error('两次密码不一致'))
      }else{
        callback()
      }
    }
    const validatePhone = (rule, value, callback) => {
      debugger
        const phoneRegex = /^1[3-9]\d{9}$/
        if (!phoneRegex.test(value)) {
          callback(new Error('请输入正确的手机号!'))
        } 
        callback()
    }
    return {
      currentUserInfo: {
        username: '',
        trueName: '',
        phone: '',
        roleName: '',
        todate: '',
        check: '',
      },
      // tab标签
      activeName: 'account',
      // 修改用户信息弹窗
      updateCurrentUserDialog:false,
      // 修改手机号弹窗
      updateCurrentPhoneDialog:false,
      // 修改密码弹窗
      updatePasswordDialog:false,
      // 更新信息表单信息
      updateUserFormRules: {
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
        trueName: [
          {
            required: true,
            message: '请输入真实姓名',
            trigger: 'blur'
          }
        ],
      },
      updatePasswordFormRules:{
        password: [
          {
            validator: validatePassword,
            trigger: 'blur'
          }
        ],
        passwordfun: [
          {
            validator: validatePasswordfun,
            trigger: 'blur'
          }
        ],
        oldPassword: [
          {
            validator: validatePassword,
            trigger: 'blur'
          }
        ],
      },
      updatePhoneFormRules: {
        phone: [
          {
            validator: validatePhone,
            trigger: 'blur'
          }
        ],
        password: [
          {
            validator: validatePassword,
            trigger: 'blur'
          }
        ],
        passwordfun: [
          {
            validator: validatePasswordfun,
            trigger: 'blur'
          }
        ],
        codePhone: [
          {
            required: true,
            message: '请输入验证码',
            trigger: 'blur'
          },
        ]
      }
    };
  },
  created() {
    this.getCurrentUser();
  },
  methods: {
    toDashboardPage() {
      router.push('/index');
    },
    getCurrentUser() {
      user.getCurrentUser().then((resp) => {
        if (resp.code === 200) {
          resp.data.password = '';
          this.currentUserInfo = resp.data;
        }
      });
    },
    updateUser(){
      this.updateCurrentUserDialog = true
    },
    updatePhone(){
      this.updateCurrentPhoneDialog = true
    },
    updatePassword(){
      this.updatePasswordDialog = true
    },
      // 更新当前用户
    updateCurrentUser (check) {
      this.currentUserInfo.check = check
      user.updateCurrentUser(this.currentUserInfo).then((resp) => {
        if (resp.code === 200) {
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
          // 更新token
          localStorage.setItem('authorization', resp.data)
          this.updateCurrentUserDialog = false
          this.getCurrentUser()
        }
      })
    },
    // tab标签点击事件
    handleClick(tab, event) {
        console.log(tab, event);
    },
    // 发送验证码
    getPhoneCode(){
      if (this.currentUserInfo.phone == ''){
        this.$notify({
          title: 'Tips',
          message: '请输入手机号',
          type: 'error',
          duration: 2000
        })
        return;
      }
      auth.sendValidateCode(this.currentUserInfo.phone).then(resp => {
        if (resp.code === 200) {
          this.$notify({
            title: 'Tips',
            message: '验证码已发送',
            type: 'success',
            duration: 2000
          })
        }
      })
    },

    updateCurrentPhone(){
      user.updateCurrentPhone(this.currentUserInfo).then((resp) => {
          if (resp.code === 200) {
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000
            })
          }
        })
      },
  },
};
</script>

<style scoped lang="scss" >
@import "../../assets/css/index/setUserInfo";

</style>