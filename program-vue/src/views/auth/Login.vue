<template>
  <el-container>
    <el-main>
      <el-card
        class="box-card"
        shadow="always"
      >
        <!-- <div
          slot="header"
          class="card-header"
        >
          <p>编程云</p>
        </div> -->

        <div>
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="账户登录" name="account">
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
            </el-tab-pane>
            <el-tab-pane label="手机号登录" name="phone">
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
                    prefix-icon="el-icon-user"
                    v-model="phoneLoginForm.phone"
                    placeholder="手机号"
                  />
                </el-form-item>
                <el-form-item prop="code">
                  <el-input
                    class="code"
                    prefix-icon="el-icon-chat-line-round"
                    v-model="phoneLoginForm.code"
                    placeholder="验证码"
                  />
                  <img
                    @click="changeCode"
                    id="phoneCode"
                    style="float: right;margin-top: 4px;cursor: pointer"
                    title="看不清,点击刷新"
                    alt="验证码"
                  >
                </el-form-item>

                <el-form-item prop="code">
                  <el-input
                    class="code"
                    prefix-icon="el-icon-chat-line-round"
                    v-model="phoneLoginForm.codePhone"
                    placeholder="手机验证码"
                  />
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
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-card>
    </el-main>

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
  },
  mounted () {
    loginFunc.changeCode()
  },
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
    }
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/css/auth/login";

</style>
