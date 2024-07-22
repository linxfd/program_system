import request from '@/utils/request'

export default {
  login (loginUser) {
    return request({
      url: '/common/login',
      method: 'post',
      data: loginUser
    })
  },
  checkToken () {
    return request({
      url: '/common/checkToken',
      method: 'get'
    })
  },
  getCode (id) {
    return request({
      url: `/util/getCode?id=${id}`,
      method: 'get'
    })
  },
  register (registerUser) {
    return request({
      url: '/common/register',
      method: 'post',
      data: registerUser
    })
  },
  checkUsername (username) {
    return request({
      url: `/common/check/${username}`,
      method: 'get'
    })
  },
  checkUserPhone (phone) {
    return request({
      url: `/common/checkPhone/${phone}`,
      method: 'get'
    })
  },
  checkeditUserPhone (data) {
    return request({
      url: '/common/checkeditUserPhone',
      method: 'post',
      data: data
    })
  },
  editUsername(data){
    return request({
      url: '/common/editUsername',
      method: 'post',
      data: data
    })
  },
}
