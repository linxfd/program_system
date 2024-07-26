import request from '@/utils/request'

export default {
  getExamPassRate (queryInfo) {
    return request({
      url: '/teacher/getExamPassRate',
      method: 'get',
      params: queryInfo
    })
  },
  getExamNumbers (queryInfo) {
    return request({
      url: '/teacher/getExamNumbers',
      method: 'get',
      params: queryInfo
    })
  }
}
