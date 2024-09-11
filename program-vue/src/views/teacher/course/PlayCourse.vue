<template>
  <el-container>
    <!-- 顶部区域放置课程基本信息 -->
    <el-header style="margin: 0 0 1.5% 0 ;">
      <h1>{{ courseBase.name }}</h1>
      <p>{{ courseBase.description }}</p>
      <!-- 可以添加更多课程基本信息 -->
    </el-header>

    <!-- 主体部分分为两列 -->
    <el-main>
      <el-row :gutter="20">
        <!-- 左侧视频播放器 -->
        <el-col :span="16">
          <video :src="currentVideoUrl" controls width="100%" height="auto"></video>
        </el-col>

        <!-- 右侧视频列表 -->
        <el-col :span="8">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>课程目录</span>
            </div>
            <ol>
              <li v-for="(item, index) in courseUnitList" :key="index">
                <div class="unitItem" @click="changeVideo(item)">
                  {{ item.name }}
                  <span class="video-duration">{{ item.videoDuration }}</span>
                </div>
              </li>
            </ol>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>

import category from '@/api/category'
import courseCategory from '@/api/courseCategory'

export default {
  data() {
    return {
      // 课程信息
      courseBase: {},
      // 课程视频集合
      courseUnitList:[],
      currentVideoIndex: 0, // 当前播放视频的索引
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
    this.getCourseInfo()
  },
  computed: {
    currentVideoUrl() {
      return this.getIconUrl(this.courseUnitList[this.currentVideoIndex]?.url);
    }
  },
  methods: {
    // 获取课程信息
    getCourseInfo () {
      category.getCourseInfo(this.$route.params.id).then((resp) => {
        if (resp.code === 200) {
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
          console.log(resp.data)
          this.courseBase = resp.data.courseBase
          this.courseUnitList = resp.data.courseUnitList
        } 
      })
    },
    changeVideo(unit) {
      this.currentVideoIndex = this.courseUnitList.findIndex(u => u.name === unit.name);
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
/* 定义卡片的基本样式 */
.box-card {
    margin-bottom: 15px;
    border: 1px solid #ebeef5;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 定义标题部分的样式 */
.box-card .clearfix span {
    font-size: 18px;
    color: #303133;
    display: block;
    padding: 10px 20px;
}

/* 定义列表项的样式 */
.unitItem {
    padding: 10px 20px;
    cursor: pointer;
    transition: background 0.3s;
    position: relative;
}

/* 鼠标悬停时改变背景色 */
.unitItem:hover {
    background-color: #f0f1fa;
}

/* 当前选中的列表项高亮显示 */
.unitItem.active {
    background-color: #e6f7ff;
    color: #409EFF;
}

/* 设置列表项中的视频时长显示位置 */
.unitItem .video-duration {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 12px;
    color: #909399;
}
</style>