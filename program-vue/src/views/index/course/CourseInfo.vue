<template>
  
  <el-container>
    <el-header>
      <!-- 轮播图 -->
      <el-carousel 
      type = "card"
      :interval="5000" 
      >
        <el-carousel-item v-for="(item,index) 
          in categoryInfo" 
          :key="index" 
          :label = "item.name">
          <img :src="getIconUrl(item.pic)" :alt="item.description"
          @click="$router.push(`/course/PlayCourse/${item.id}`)"
          >
        </el-carousel-item>
      </el-carousel>
    </el-header>
    
  </el-container>
</template>

<script>

import category from '@/api/category'
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
      category.getCourseList(this.queryInfo).then((resp) => {
        if (resp.code === 200) {
          console.log(resp.data)
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