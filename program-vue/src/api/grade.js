import request from '@/utils/request'

export default {
  getMyGrade (query) {
    return request({
      url: '/student/getMyGrade',
      method: 'get',
      params: query
    })
  },
  getCertificate (query) {
    return request({
      url: '/student/getCertificate',
      method: 'get',
      // 一定要设置响应类型，否则页面会是空白pdf
      responseType: 'arraybuffer',
      params: query
    })
  }
}
