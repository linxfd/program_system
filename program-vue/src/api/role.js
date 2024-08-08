import request from '@/utils/request'

export default {
  getRoleInfo () {
    return request({
      url: '/admin/role/getRole',
      method: 'get'
    })
  },
  DoAssignMenuIdToSysRole (assignMenuDto) {
    return request({
      url: '/admin/role/doAssign',
      method: 'post',
      data: assignMenuDto
    })
  },
  addRole (addForm) {
    return request({
      url: '/admin/role/addRole',
      method: 'post',
      data: addForm
    })
  },
  updateRole (addForm) {
    return request({
      url: '/admin/role/updateRole',
      method: 'post',
      data: addForm
    })
  },
  deleteRole (id) {
    return request({
      url: `/admin/role/deleteRole/${id}`,
      method: 'get'
    })
  },
    // 获取所有角色是老师的用户
    getCreatePersonName(){
      return request({
        url: '/public/getCreatePersonName',
        method: 'get',
      })
    },
}
