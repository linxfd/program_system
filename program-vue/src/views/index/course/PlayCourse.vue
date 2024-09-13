<template>
  <el-container>
    <!-- 顶部区域放置课程基本信息 -->
    <el-header >
      <h1>{{ courseBase.name }}</h1>
    </el-header>

    <!-- 主体部分分为两列 -->
    <el-main>
      <el-row :gutter="20">
        <!-- 左侧视频播放器 -->
        <el-col :span="15">
          <video :src="currentVideoUrl" controls width="100%" height="auto"></video>
          <div class="like-button" >
            <img :src="currentImage" alt="Like" @click="toggleLike"/>  
            <span>{{ likeCount }}</span>
          </div>
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
      <div>
        <p>简介:{{ courseBase.description }}</p>
      </div>
    </el-main>
  </el-container>
</template>

<script>


import course from '@/api/course'
import common from '@/api/common'
import courseCategory from '@/api/courseCategory'

export default {
  data() {
    return {
      //用户是否点赞了
      isLike: false,
      // 点赞数量
      likeCount: 0,
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
      likedImage: require('@/assets/imgs/AfterLike.svg'),
      dislikedImage: require('@/assets/imgs/BeforeLike.svg')
    }
  },
  props: ['tagInfo'],
  created () {
    let flag = localStorage.getItem(`PlayCourse/${this.$route.params.id}`)
    if(!flag){
      this.$confirm('请从官方渠道进入课程学习页面', '提示', {  
          confirmButtonText: '确定',  
          cancelButtonText: '取消',  
          type: 'warning'  
        }).then(() => {  
          // 用户点击了确定  
          this.$router.push('/course')
        }).catch(() => {  
          // 用户点击了取消  
          this.$router.push('/course')
        });
        
    }
    // 查询第一次
    this.getCourseInfo()
    // 一创建就改变头部的面包屑
    this.$emit('giveChildChangeBreakInfo', "课程学习", "课程学习")
    // 向父组件中添加头部的tags标签
    this.createTagsInParent()

    //查询用户点赞情况和总点赞量
    this.queryLikes()
  },
  computed: {
    // 使用计算属性来根据 isLike 的值动态选择图片  
    currentImage() {

      return this.isLike ? this.likedImage : this.dislikedImage;  
    },
    currentVideoUrl() {
      return this.getIconUrl(this.courseUnitList[this.currentVideoIndex]?.url);
    }
  },
  methods: {
    // 向父组件中添加头部的tags标签
    createTagsInParent () {
      let flag = false
      this.tagInfo.map(item => {
        // 如果tags全部符合
        if (item.name === "课程学习" && item.url === this.$route.path) {
          flag = true
        } else if (item.name === "课程学习" && item.url !== this.$route.path) {
          this.$emit('updateTagInfo', "课程学习", this.$route.path)
          flag = true
        }
      })
      if (!flag) this.$emit('giveChildAddTag', "课程学习", this.$route.path)
    },
    // 获取课程信息
    getCourseInfo () {
      course.getCourseInfo(this.$route.params.id).then((resp) => {
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
    toggleLike() {
      this.isLike = !this.isLike;
      this.likeCount += this.isLike ? 1 : -1;
      common.likeAndUnlike(1,{itemId:this.$route.params.id}).then(resp => {
        if(resp.code == 200){
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
        }
      })
    },
    queryLikes(){
      common.queryLikes(1,{itemId:this.$route.params.id}).then(resp => {  
        if(resp.code == 200){
            this.isLike = resp.data.isLike;
            this.likeCount = resp.data.likeCount;
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

/* 添加点赞按钮的样式 */
.like-button {
  display: flex;
  align-items: center;
  margin: 6px 0 3px 97%;
}

.like-button img {
  width: 24px; /* 调整图标大小 */
  height: 24px;
  cursor: pointer;
}

.like-button span {
  margin-left: 5px;
  font-size: 14px;
}
</style>