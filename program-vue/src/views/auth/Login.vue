<template>
  <el-container class="el-container">
    <div class="main">
        <div class="from-login-left">
                <div class="sys-name2">编程云</div>
          <div class="sys-name1">一个学习编程的综合资源分享平台</div>

        </div>
        <div class="from-login-right">
          <img class="login-logo" src="~@/assets/imgs/logo1.png" />
          <!-- <div class="login-title">登录</div> -->
          <el-tabs v-model="activeName" @tab-click="handleClick" class="login-tab">
            <el-tab-pane label="账户登录"  name="account" class="custom-font-size">
              <div class="login-from" > 
                <el-form
                  :model="loginForm"
                  :rules="loginFormRules"
                   ref="loginForm"
                  :status-icon="true"
                  label-width="100px"
                  @keyup.enter.native="onEnterLogin"
                >
                  <el-form-item prop="username">
                    <el-input
                      prefix-icon="el-icon-user"
                      v-model="loginForm.username"
                      placeholder="账号"
                    />
                  </el-form-item>

                  <el-form-item prop="password">
                    <el-input
                      prefix-icon="el-icon-lock"
                      v-model="loginForm.password"
                      placeholder="密码"
                      show-password
                    />
                  </el-form-item>

                  <el-form-item prop="code">
                    <el-input
                      class="code"
                      prefix-icon="el-icon-chat-line-round"
                      v-model="loginForm.code"
                      placeholder="验证码"
                    />
                    <img
                      @click="changeCode"
                      id="code"
                      style="float: right;margin-top: 4px;cursor: pointer"
                      title="看不清,点击刷新"
                      alt="验证码"
                    >
                  </el-form-item>

                  <el-form-item>
                    <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
                  </el-form-item>

                  <el-form-item>
                    <el-button
                      type="primary"
                      @click="login($refs['loginForm'])"
                      icon="el-icon el-icon-s-promotion"
                    >
                      登录
                    </el-button>
                    <el-button
                      type="warning"
                      @click="toRegisterPage"
                      icon="el-icon el-icon-circle-plus"
                    >
                      学员注册
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>
            <el-tab-pane label="手机登录" name="phone">
              <div class="login-from" > 
                <el-form
                  :model="phoneLoginForm"
                  :rules="phoneLoginFormRules"
                  ref="phoneLoginForm"
                  :status-icon="true"
                  label-width="100px"
                  @keyup.enter.native="onEnterPhoneLogin"
                >
                  <el-form-item prop="phone">
                    <el-input
                      prefix-icon="el-icon-phone-outline"
                      v-model="phoneLoginForm.phone"
                      placeholder="手机号"
                    />
                  </el-form-item>
                  <el-form-item prop="code">
                    <el-input
                      class="code"
                      prefix-icon="el-icon-chat-line-round"
                      v-model="phoneLoginForm.codePhone"
                      placeholder="手机验证码"
                    />
                    <button class="zbutton" @click="getPhoneCode">获取验证码</button>
                    <p class="p-phone">未注册手机号验证通过后将自动注册</p>
                  </el-form-item>



                  <el-form-item>
                    <el-button
                      type="primary"
                      @click="phoneLogin($refs['phoneLoginForm'])"
                      icon="el-icon el-icon-s-promotion"
                    >
                      登录
                    </el-button>
                    <el-button
                      type="warning"
                      @click="toRegisterPage"
                      icon="el-icon el-icon-circle-plus"
                    >
                      学员注册
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
              </el-tab-pane>
          </el-tabs>
        </div>

      </div>
    <Footer />
  </el-container>
</template>

<script>
import Footer from '@/components/Footer'
import LoginFunc from '@/function-namespace/auth/LoginFunc'
import loginFunc from '@/function-namespace/auth/LoginFunc'
import utils from '@/utils/utils'

export default {
  name: 'Login',
  components: { Footer },
  data () {
    return {
      ...LoginFunc,
      captchaUrl: process.env.VUE_APP_CAPTCHA_URL,
      // tab标签
      activeName: 'account'
    }
  },
  created () {
    // 检验用户是否存在token,存在直接跳转主页
    utils.checkToken('/index')
    // 如果上次用户勾选记住我
    let loginMe = JSON.parse(localStorage.getItem('rememberMe'));
    if(loginMe.username != ''){
      this.loginForm.username = loginMe.username
      this.loginForm.password = loginMe.password
    }
  },
  mounted () {
    loginFunc.changeCode()
  },
  // updated () {
  // },
  methods: {
    onEnterLogin () {
      this.login(this.$refs.loginForm)
    },
    onEnterPhoneLogin () {
      this.phoneLogin(this.$refs.phoneLoginForm)
    },
    // tab标签点击事件
    handleClick(tab, event) {
        console.log(tab, event);
    },
    getPhoneCode(){
      if (this.phoneLoginForm.phone == ''){
        this.$notify({
          title: 'Tips',
          message: '请输入手机号',
          type: 'error',
          duration: 2000
        })
        return;
      }
      this.sendCode()
    },
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/css/auth/login";

  .zbutton {
    width: 23%;
    height: 10%;
    padding: 10px 10px;
    color: #333;
    position: relative;
    margin: 10px; /* 设置项目的外边距 */
    font-size: 2px;
    text-align: center;
    text-decoration: none;
    color: #333;
    background-color: white;
    border: 2px solid #e3e2e2;
    border-radius: 4px;
    cursor: pointer;
    transition: all 0.3s ease;
    overflow: hidden;
  }
  .p-phone{
    color: #999;
  }

</style>

