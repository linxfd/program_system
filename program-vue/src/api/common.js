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

      // 点赞或取消点赞（type,itemId）
      likeAndUnlike(type, itemId) {
        return request({
          url: `/public/userInteract/likeAndUnlike/${type}`,
          method: 'get',
          params: itemId
        })
      },
      //查询用户点赞情况和总点赞量
      queryLikes(type, itemId) {
        return request({
          url: `/public/userInteract/queryLikes/${type}`,
          method: 'get',
          params: itemId
        })
      },
      
}