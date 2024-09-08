<template>
  <el-container>
    <el-header height="220">
      <el-steps
        :active="curStep"
        simple
      >
        <el-step
          title="课程信息"
          icon="el-icon-edit"
        />
        <el-step
          title="视频列表"
          icon="el-icon-lock"
        />
        <el-step
          title="积分设置"
          icon="el-icon-setting"
        />
      </el-steps>

      <el-button
        style="margin-top: 10px"
        v-show="curStep !== 1"
        @click="curStep--"
      >
        上一步
      </el-button>

      <el-button
        style="float:right;margin-top: 10px"
        v-show="curStep !== 3"
        type="primary"
        @click="curStep++"
      >
        下一步
      </el-button>
      <el-button
        style="float:right;margin-top: 10px"
        v-show="curStep === 3"
        type="primary"

      >
        提交
      </el-button>
      <br/>

    </el-header>

    <el-main>
      <!--设置试题信息-->
      <el-card v-show="curStep === 1">
        <div>
          <el-card>
            <el-form
              :model="categoryInfo"
              :rules="examInfoRules"
              ref="examInfoForm"
              label-width="100px"
            >
              <el-form-item
                label="课程名称"
                prop="name"
              >
                <el-input v-model="categoryInfo.name" />
              </el-form-item>

              <el-form-item
                label="课程介绍"
                prop="description"
              >
                <el-input v-model="categoryInfo.description" 
                type="textarea"
                :rows="4"
                />
              </el-form-item>

              <el-form-item
                label="课程分类"
              >
                <el-cascader  
                  :options="categoryOptions"  
                  :props="cascaderProps"  
                  style="margin-left: 5px"
                  placeholder="搜索分类"
                  v-model="selectedCategories"  
                  clearable  
                  @change="handleChange"  
                />
              </el-form-item>
              
              
              <el-form-item
                label="课程首页图片"
              >
                <el-upload
                    ref="upload"
                    :action="uploadImageUrl + '/teacher/uploadQuestionImage'"
                    name="file"
                    drag
                    :headers="headers"
                    :before-upload="beforeUploadPic"
                    :on-success="importFile"
                    v-loading="Uploadloading"
                  >
                    <div  class="placeholder">
                      <div v-if="!this.categoryInfo.pic">
                          <i class="el-icon-upload"></i>
                          <div class="el-upload__text">将图片文件拖到此处，或<em>点击上传</em></div>
                      </div>
                      <img v-else :src="getIconUrl(this.categoryInfo.pic)" alt=""  class="preview-image">
                    </div>
                </el-upload>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-card>


      <el-card v-show="curStep === 2">
        <div id="app">
          <el-card v-for="(card, index) in cards" :key="index" class="box-card">
            <el-input v-model="card.courseName" placeholder="请输入课程单元名称"></el-input>
            <el-upload
              class="upload-demo"
              drag
              action="your/upload/url"
              :file-list="fileList"
              :on-success="handleUploadSuccess"
              :before-upload="beforeUpload"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">只能上传视频文件</div>
            </el-upload>
          </el-card>
          <el-button @click="addCard" type="primary" style="margin-top: 20px;">添加课程单元</el-button>
        </div>
      </el-card>


      <el-card v-show="curStep === 3">
        <el-card>

          <el-radio-group
            v-model="categoryInfo.charge"
            size="medium"
          >
            <el-radio
              :label="1"
              border
            >
              完全公开
            </el-radio>
            <el-radio
              :label="2"
              border
            >
              需要积分
            </el-radio>
          </el-radio-group>

          <el-alert
            style="margin-top: 15px"
            :title="categoryInfo.charge === 1? '开放的，任何人都可以进行考试！' : '半开放的,需要积分兑换课程!'"
            type="warning"
          />

          <el-input-number
            style="margin-top: 15px;width: 20%"
            v-model="categoryInfo.pointsNumber"
            v-show="categoryInfo.charge === 2"
            placeholder="输入积分数量"
          />
        </el-card>
      </el-card>


    </el-main>


  </el-container>
</template>

<script>
import utils from '@/utils/utils'
import { generateSign } from '@/utils/sign'
import category from '@/api/category'
import courseCategory from '@/api/courseCategory'

export default {
  name: 'CourseBaseModel',
  data () {
    return {
      //课程视频列表
      cards: [
        { // 初始化一个
          courseName: '',
          videoUrl: ''
        }
      ],
      fileList: [],
      uploadImageUrl: process.env.VUE_APP_UPLOAD_IMAGE_URL,
      // minio的主机名
      minioUrl: process.env.VUE_APP_MINIO_URL,
      // 当前的步骤
      curStep: 1,
      // 对话框中题目表格的加载
      loading: true,
      // 数据预加载
      Uploadloading: false,
      // 当前课程的信息
      categoryInfo: {
        pic : '',
      },
      // 表单验证
      examInfoRules: {
      },
      selectedCategories: [],  
      categoryOptions: [],  
      //分类的配置
      cascaderProps: {
        value: 'id',  // 使用 id 作为唯一标识符  
        label: 'label',  
        children: 'children',  
        // disabled: 'isDisabled',  
        checkStrictly: false,
      },
      courseCategoryName : "添加课程" ,
    }
  },
  props: ['tagInfo'],
  created () {
    // 一创建就改变头部的面包屑
    this.$emit('giveChildChangeBreakInfo', this.courseCategoryName, this.courseCategoryName)
    this.createTagsInParent()
    // 查询分类数据
    this.fetchData()
  },
  methods: {
    // 向父组件中添加头部的tags标签
    createTagsInParent () {
      let flag = false
      this.tagInfo.map(item => {
        // 如果tags全部符合
        if (item.name === this.courseCategoryName && item.url === this.$route.path) {
          flag = true
        } else if (item.name === this.courseCategoryName && item.url !== this.$route.path) {
          this.$emit('updateTagInfo', this.courseCategoryName, this.$route.path)
          flag = true
        }
      })
      if (!flag) this.$emit('giveChildAddTag', this.courseCategoryName, this.$route.path)
    },

    // 选择分类
    handleChange(){
      this.categoryInfo.mt = this.selectedCategories[0]
      this.categoryInfo.st = this.selectedCategories[1]
    },
    // 查询分类数据
    fetchData () {
      courseCategory.FindNodes().then((resp) => {
        if (resp.code === 200) {
          this.categoryOptions = resp.data
          console.log(this.categoryOptions)
        }
        this.$notify({
          title: 'Tips',
          message: resp.message,
          type: 'success',
          duration: 2000
        })
      })
    },    
    //导入图标前
    beforeUploadPic(file) {
      const isImage = file.type.indexOf('image/') === 0;
      if (!isImage) {
        this.$message.error('只能上传图片文件！');
        this.Uploadloading = false; // 确保加载状态关闭
        return false;
      }

      this.Uploadloading = false; // 无论结果如何，都关闭加载状态
      return isImage;
    },
    // 导入图标后
    importFile(val){
      this.Uploadloading = false
      this.categoryInfo.pic = val.data
      // 清除上传列表
      this.$refs.upload.clearFiles();
    },

    addCard() {
      this.cards.push({
        courseName: '',
        videoUrl: '',
      });
    },
    handleUploadSuccess(response, file, fileList) {
      this.categoryInfo.pic = response.data
      // 处理上传成功后的逻辑
      console.log('文件上传成功', response, file, fileList);
    },
    beforeUpload(file) {
      const isVideo = file.type.indexOf('video') === 0;
      if (!isVideo) {
        this.$message.error('只能上传视频文件！');
      }
      return isVideo;
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
  computed: {
    // 监测头部信息的token变化
    headers () {
      const signHeaders = {
        'body-string': '',
        'query-string': '',
        'x-nonce': `${utils.getRandomId()}`,
        'x-timestamp': `${new Date().getTime()}`,
        'description': "Course home picture"
      }
      return {
        ...signHeaders,
        authorization: localStorage.getItem('authorization') || '',
        sign: generateSign(JSON.stringify(signHeaders))
      }
    },

  }
}
</script>

<style scoped lang="scss">
.el-container {
  width: 100%;
  height: 100%;
}

.el-container {
  animation: leftMoveIn .7s ease-in;
}

@keyframes leftMoveIn {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0%);
    opacity: 1;
  }
}

/deep/ .el-table thead {
  color: rgb(85, 85, 85) !important;
}

/*表格的头部样式*/
/deep/ .has-gutter tr th {
  background: rgb(242, 243, 244);
  color: rgb(85, 85, 85);
  font-weight: bold;
  line-height: 32px;
}

.el-table {
  box-shadow: 0 0 1px 1px gainsboro;
}






.placeholder p {
  
  margin: 5px 0;
}
.preview-image {
  // width: 200px; /* 固定宽度 */
  // height: auto; /* 自动调整高度以保持原始宽高比 */
  /* 或者使用百分比 */
  width: 100%; 
  height: auto;
}
.box-card {
  margin-bottom: 20px;
}
</style>
