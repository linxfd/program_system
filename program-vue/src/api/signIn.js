import request from '@/utils/request'
import { add } from 'lodash'

export default {
    // 签到
    signIn (data) {
        return request({
          url: '/public/sign/signIn',
          method: 'post',
          data: data
        })
    },
    // 获得初始数据
    //累计签到积分、连续的签到次数
    getSignCountInfo(date){
        return request({
          url: `/public/sign/getSignCountInfo/${date}`,
          method: 'get',
        })
      },
    // 获取签到信息
    getSignInfo(dateStr){
        return request({
          url: `/public/sign/getSignInfo/${dateStr}`,
          method: 'get',
        })
    },
    // 获取签到信息
    getContinuousSignCount(date){
      return request({
        url: `/public/sign/getContinuousSignCount/${date}`,
        method: 'get',
      })
    },
    // 获取积分信息列表
    getList(queryParams){
      return request({
        url: `/public/sign/list`,
        method: 'post',
        data: queryParams,
      })
    },
    // 添加积分信息
    add(queryParams) {
      return request({
        url: `/public/sign/add`,
        method: 'post',
        data: queryParams,
      })
    },
    edit(queryParams){
      return request({
        url: `/public/sign/edit`,
        method: 'post',
        data: queryParams,
      })
    },
    remove(ids){
      return request({
        url: `/public/sign/remove/${ids}`,
        method: 'get',
      })
    }
    
}