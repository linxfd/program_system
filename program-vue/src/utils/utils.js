import auth from '@/api/auth'
import Vue from 'vue'
import router from '../router/index'

const getRandomId = () => {
  return Math.floor(Math.random() * 10000000)
}

// valid el form and invoke target method
const validFormAndInvoke = (formEl, success, message = '信息有误', fail = function () {
}) => {
  if (!formEl) {
    return
  }
  formEl.validate(valid => {
    if (valid) { // form valid succeed 表格有效成功
      // do success function 执行成功函数
      success()
      // reset fields 重置的字段
      formEl.resetFields()
    } else { 
      Vue.prototype.$notify({
        title: 'Tips',
        message: message,
        type: 'error',
        duration: 2000
      })
      // do something when fail失败时做点什么
      fail()
      return false
    }
  })
}
// check token and router link 检查令牌和路由器链路
const checkToken = (to) => {
  if (localStorage.getItem('authorization') !== null) {
    auth.checkToken()
      .then(resp => {
        if (resp.code === 200) {
          router.push(to)
        }
      })
      .catch(error => {
        localStorage.removeItem('authorization')
      })
  }
}

export default {
  validFormAndInvoke,
  checkToken,
  getRandomId
}
