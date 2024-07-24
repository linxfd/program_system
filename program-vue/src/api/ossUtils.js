import request from '@/utils/request'

export default {
  uploadImage (imageObj) {
    return request({
      url: '/student/uploadQuestionImage',
      method: 'post',
      data: imageObj
    })
  }
}
