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
        type="primary"
        @click="backtrack"
      >
        返回
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
        @click="submit"
      >
        提交
      </el-button>
      
      <el-button
        style="float:right;margin-top: 10px"
        v-show="curStep !== 1"
        @click="curStep--"
      >
        上一步
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
                  @change="handleChange"  
                />
              </el-form-item>
            
              <el-form-item
                label="课程首页图片"
              >
                <el-upload
                    ref="upload"
                    :action="uploadImageUrl + '/teacher/uploadImage'"
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
            <el-button @click="removeCard(index)" type="text" style="float: right;">
              <i class="el-icon-close"  style="font-size: 24px;"></i>
            </el-button>
            <div class="card-number"> 课程{{ index + 1 }}</div> 
            <el-input  v-model="cards[index].name" placeholder="请输入课程单元名称"></el-input>
            <br><br>
            <el-upload
              :action="uploadImageUrl + '/teacher/uploadVideo'"
              :headers="headers"
              :on-success="handleUploadSuccess.bind(this, index)" 
              :before-upload="beforeUpload"
            >
                <div>
                  <video v-if="cards[index].url" controls style="width: 500px;  ">  
                    <source :src="getIconUrl(cards[index].url)" type="video/mp4">  
                        Your browser does not support the video tag.  您的浏览器不支持视频标签。
                  </video> 
                </div>
                <el-button
                  size="small"
                  type="primary"
                >
                  点击上传
                </el-button>
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
          name: '',
          url: ''
        }
      ],
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
        charge: 1,
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
    if(this.$route.params.id != 0){
      this.courseCategoryName = "编辑课程"
      // 查询需要编辑的课程信息
      this.getCategoryInfo()
    }
    // 一创建就改变头部的面包屑
    this.$emit('giveChildChangeBreakInfo', this.courseCategoryName, this.courseCategoryName)
    // 向父组件中添加头部的tags标签
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
    // 查询需要编辑的课程信息
    getCategoryInfo(){
      const id = this.$route.params.id
      category.getCategoryInfo(id).then(res => {
        if(res.code == 200){
          this.$notify({
            title: 'Tips',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.cards = res.data.cards
          this.categoryInfo = res.data.courseBase
          //复原分类信息
          this.recoverChange()
        }
        
      })
    },

    // 选择分类
    handleChange(){
      console.log(this.selectedCategories)
      this.categoryInfo.mt = this.selectedCategories[0]
      this.categoryInfo.st = this.selectedCategories[1]
    },
    // 复原分类列表
    recoverChange(){
      this.$set(this.selectedCategories, 0,Number(this.categoryInfo.mt));
      this.$set(this.selectedCategories, 1,Number(this.categoryInfo.st));
    },
    // 查询分类数据
    fetchData () {
      courseCategory.FindNodes().then((resp) => {
        if (resp.code === 200) {
          this.categoryOptions = resp.data
          this.$notify({
            title: 'Tips',
            message: resp.message,
            type: 'success',
            duration: 2000
          })
        }
        
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

    removeCard(index) {
          // 删除指定索引的卡片
          this.cards.splice(index, 1);
        },
    addCard() {
      this.cards.push({
        name: '',
        url: ''
      });
    },
    handleUploadSuccess(index, response, file, fileList) { 
      this.cards[index].url = response.data; // 假设服务器返回的url在data.url中
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
    backtrack(){
      this.$router.push('/course/courseBase')
    },
    submit(){
      const data = {
        courseBase: this.categoryInfo,
        cards: this.cards,
      }
      if(this.$route.params.id == 0 ){
        category.addCategory(data).then((resp) => {
          if (resp.code === 200) {
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'success',
                duration: 2000
              })
              this.$router.push('/course/courseBase')
            }
        })
      }else{
        category.updateCourse(data).then((resp) => {
          if (resp.code === 200) {
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'success',
                duration: 2000
              })
              this.$router.push('/course/courseBase')
            }
        })
      }
      
    }
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
.card-number {  
  margin-bottom: 10px; /* 示例：给序号添加一些底部外边距 */  
  font-weight: bold; /* 示例：将序号加粗 */  
}  
</style>
