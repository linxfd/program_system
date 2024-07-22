import request from '@/utils/request'

export default {
  getCurrentUser () {
    return request({
      url: '/common/getCurrentUser',
      method: 'get'
    })
  },
  updateCurrentUser (updateUser) {
    return request({
      url: '/common/updateCurrentUser',
      method: 'post',
      data: updateUser
    })
  },
  updateUser (user) {
    return request({
      url: '/common/updateUser',
      method: 'post',
      data: user
    })
  },
  getUserById (userId) {
    return request({
      url: `/teacher/getUserById/${userId}`,
      method: 'get'
    })
  },
  getUserByIds (userIds) {
    return request({
      url: `/teacher/getUserByIds`,
      params: userIds,
      method: 'get'
    })
  },
  getUserInfo (data) {
    return request({
      url: '/admin/getUser',
      method: 'post',
      data: data
    })
  },
  handlerUser (operateId, params) {
    return request({
      url: `/admin/handleUser/${operateId}`,
      method: 'get',
      params: params
    })
  },
  addUser (data) {
    return request({
      url: '/admin/addUser',
      method: 'post',
      data: data
    })
  },

  deleteUser (id) {
    return request({
      url: `/admin/removeUserById/${id}`,
      method: 'get',
    })
  },
  
}
