
import request from '@/utils/request'

export default {
  // 分页列表
  FindNodes () {
    return request({
      url: '/teacher/category/findNodes',
      method: 'get'
    })
  },
  // 保存信息
  SaveCategory (courseCategory) {
    return request({
      url: '/teacher/category/save',
      method: 'post',
      data: courseCategory
    })
  },
  // 修改信息
  UpdateCourseCategoryById(courseCategory) {
    return request({
      url: '/teacher/category/updateById',
      method: 'put',
      data: courseCategory
    })
  },

  // 根据id删除数据
  RemoveCourseCategoryById (id) {
    return request({
      url: `/teacher/category/removeById/${id}`,
      method: 'delete'
    })
  }
}
