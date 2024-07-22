import request from '@/utils/request'

export default {
  getRoleInfo () {
    return request({
      url: '/role/getRole',
      method: 'get'
    })
  },
  DoAssignMenuIdToSysRole(assignMenuDto) {
    return request({
      url: '/role/doAssign',
      method: 'post',
      data: assignMenuDto,
    })
  }
}
