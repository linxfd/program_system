<template>
  
  <el-container>
    <el-header>
      <!-- 轮播图 -->
      <el-carousel 
      type = "card"
      :interval="5000" 
      >
        <el-carousel-item v-for="(item,index) in categoryInfo" 
          :key="index" 
          :label = "item.name">
          <img :src="getIconUrl(item.pic)" :alt="item.description"
            @click="goCourse(item)"
            >
        </el-carousel-item>
      </el-carousel>
    </el-header>
    
  </el-container>
</template>

<script>

import course from '@/api/course'
import common from '@/api/common'
import courseCategory from '@/api/courseCategory'

export default {
  data() {
    return {
      // 课程信息
      categoryInfo: [],
      queryInfo: {
        pageNo: 1,
        pageSize: 10
      },
      // 所有数据的条数
      total: 0,
      // minio的主机名
      minioUrl: process.env.VUE_APP_MINIO_URL,
    }
    
  },
  created () {
    // 查询第一次
    this.getListInfo()
  },
  methods: {
    // 获取课程信息
    getListInfo () {
      course.getCourseList(this.queryInfo).then((resp) => {
        if (resp.code === 200) {
          this.categoryInfo = resp.data.data
          this.total = resp.data.total
        } else {
          this.$notify({
            title: 'Tips',
            message: '获取信息失败',
            type: 'error',
            duration: 2000
          })
        }
      })
    },
    goCourse(value){
      if(value.charge == 1){
        localStorage.setItem(`PlayCourse/${value.id}`,true)
        this.$router.push(`/course/PlayCourse/${value.id}`)
      }else{
        common.getRedemptionStatus(1,{itemId:value.id}).then((resp) => {
          if(resp.code == 200){
            if(resp.data.isRedeemed == 1){
              this.$confirm(`该课程需要${resp.data.pointsNumber}是否兑换?`, '提示', {  
                confirmButtonText: '确定',  
                cancelButtonText: '取消',  
                type: 'warning'  
              }).then(() => {
                // 用户点击了确定,兑换课程
                common.redemption(1,{itemId:value.id}).then((resp) => {
                  if(resp.data == true){
                    this.$message({
                      type: 'success',
                      message: '兑换成功!'
                    })
                    localStorage.setItem(`PlayCourse/${value.id}`,true)
                    this.$router.push(`/course/PlayCourse/${value.id}`)
                  }else{
                    // 兑换失败，如用户积分不够
                    this.$message({
                      type: 'error',
                      message: resp.message
                    })
                  }
                })
              }).catch(() => {  
                // 用户点击了取消  
              });
            }else{
              localStorage.setItem(`PlayCourse/${value.id}`,true)
              this.$router.push(`/course/PlayCourse/${value.id}`)
            }
          }
        })
        
      }
    },
    getIconUrl(iconPath) {
      // 检查iconPath是否包含协议头（例如http://或https://）
      if (/^https?:\/\//.test(iconPath)) {
        // 如果iconPath已经是完整的URL，直接返回
        return iconPath;
      } else {
        // 如果不是完整的URL，则拼接基础URL
        return `${this.minioUrl}${iconPath}`;
      }
    }, 
  },
} 
</script>

<style>
 .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
  }
  
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>