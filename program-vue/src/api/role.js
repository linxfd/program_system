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
  },
  addRole(addForm){
    return request({
      url: '/role/addRole',
      method: 'post',
      data: addForm,
    })
  },
  updateRole(addForm){
    return request({
      url: '/role/updateRole',
      method: 'post',
      data: addForm,
    })
  },
  deleteRole(id){
    return request({
      url: `/role/deleteRole/${id}`,
      method: 'get',
    })
  },
}
