import request from '@/utils/request'

export default {
    // 签到
    signIn (data) {
        return request({
          url: '/public/sign/signIn',
          method: 'post',
          data: data
        })
    },
    // 获取签到次数
    getSignCount(date){
        return request({
          url: `/public/sign/getSignCount/${date}`,
          method: 'get',
        })
      },
    // 获取签到信息
    getSignInfo(dateStr){
        return request({
          url: `/public/sign/getSignInfo/${dateStr}`,
          method: 'get',
        })
    }

}