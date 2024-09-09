import request from '@/utils/request'

export default {
  uploadImage (imageObj) {
    return request({
      url: '/student/uploadImage',
      method: 'post',
      data: imageObj
    })
  }
}
