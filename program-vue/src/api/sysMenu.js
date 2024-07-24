import request from '@/utils/request'

export default {
  // 分页列表
  FindNodes () {
    return request({
      url: '/admin/sysMenu/findNodes',
      method: 'get',
    })
  },
  // 分页列表
  GetSysRoleMenuIds (id) {
    return request({
      url: `/admin/sysMenu/findNodes/${id}`,
      method: 'get',
    })
  },
  // 保存信息
  SaveMenu (sysMenu){
    return request({
      url: '/admin/sysMenu/save',
      method: 'post',
      data: sysMenu,
    })
  },
  // 修改信息
  UpdateSysMenuById (sysMenu) {
    return request({
      url: '/admin/sysMenu/updateById',
      method: 'put',
      data: sysMenu,
    })
  },
  // 根据id删除数据
  RemoveSysMenuById (id) {
    return request({
      url: `/admin/sysMenu/removeById/${id}`,
      method: 'delete',
    })
  }
}
