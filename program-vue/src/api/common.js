import request from '@/utils/request'

export default {
    // 获取兑换状态（data:{userId:xxx,types:xxx}）
    getRedemptionStatus (type, itemId) {
        return request({
          url: `/public/getRedemptionStatus/${type}`,
          method: 'get',
          params: itemId
        })
      },
      // 获取兑换状态（type,itemId）
      redemption(type, itemId) {
        return request({
          url: `/public/redemption/${type}`,
          method: 'get',
          params: itemId
        })
      },
}