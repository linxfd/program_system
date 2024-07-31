<template>
<el-container>
  <el-header>
    <h1 class="title">
      网站推荐
    </h1>
    
  </el-header>
  <el-main>

    <div class="container">
        <div
          v-for="item in classificationList"
          :key="item.id"
          class="divMain classidiedbutton"
          @click="selectItem(item.id)"
          :class="currentIndex === item.id? 'black' : 'white'"
        >
            <p class="p_classidied">{{item.name}}</p>
          </div>
    </div>

    <div class="container">
        <div
          v-for="(item,index) in response.data"
          :key="index"
          class="divMain button-3d"
          @click="openBaiduInNewTab(item.url)"
        >
          <img :src="item.icon" :alt="item.notes" style="width: 50px;height: 50px;">
          <div>
            <p class="p_button">{{item.name}}</p>
            <p class="p">{{item.notes}}</p>
          </div>
      </div>
    </div>
  </el-main>
  </el-container>
</template>

<script>
import website from '@/api/website'
export default {

  name: 'Dashboard',
  data(){
    return{
      response:{},
      classificationList:{},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        notes: null,
        name: null,
        isDeleted: null,
        orderByColumn: 'id',
        classified:'',
        isAsc: 'desc'
      },
      currentIndex: 0 // 初始化为 null 表示没有元素被选中
    }
  },
  created () {
    
    this.getList()
    // this.getClassidied()
    this.getClassificationList()
  },
  methods:{
    selectItem(index) {
      this.currentIndex = index; // 更新当前选中的元素索引
      
      this.queryParams.classificationId = index;
      // 如果当前索引为 0 “全部”，则将 classified 设置为空字符串
      if (this.currentIndex === 0) {
        this.queryParams.classificationId = '';
      }
      this.getList()
    },
    getList() {
        website.getList(this.queryParams).then((resp) => {
          if (resp.code == 200){
              this.response = resp.data;
          }
        });
    },
    getClassificationList() {
      const queryParams = {
              pageNum: 1,
              pageSize: 999,
            }
      website.getClassificationList(queryParams).then((resp) => {
        if (resp.code == 200){
            this.classificationList = resp.data.data;
            // 在数组开头添加一个元素
            this.classificationList.unshift({id:0,name:'全部'});
        }
      });
    },
  openBaiduInNewTab(vle){
    window.open(vle, '_blank');  
  },
  }
}
</script>

<style scoped lang="scss">
  div {
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

  .title {
    text-align: center;
    font-size: 25px;
  }

  .p_classidied {
    text-indent: 2em;
    font-size: 20px;
  }
  .p_button {
    color: #0a29d7;
    text-indent: 2em;
    font-size: 22px;
    text-align: right;
  }

  a {
    text-decoration: none
    // text-align: right;
  }
  .container{
    display: flex;
    flex-wrap: wrap; /* 允许元素换行 */
  }
  .divMain{
    justify-content: space-between; 		/* 用于控制弹性容器中子元素的水平对齐方式。*/
    display: flex ;
  }

  .black {
    background-color: rgb(34, 31, 31);
    color: white; /* 文字颜色也需要调整为白色以提高可读性 */
  }

.white {
  background-color: white;
  color: black; /* 文字颜色也需要调整为黑色以提高可读性 */
}
  /* 导航按钮样式 */
.classidiedbutton{
  position: relative;
  padding: 12px 24px;
  margin: 10px; /* 设置项目的外边距 */

  text-align: center;
  text-decoration: none;
  border: 2px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15), 0 2px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border-radius: 30px;
}

  /* 导航列表样式 */
.button-3d {
  flex: 0 0 calc(20% - 20px);/* 计算每个项目的宽度为容器宽度的1/3，并减去左右间距的一半 */
  position: relative;
  padding: 12px 24px;
  margin: 10px; /* 设置项目的外边距 */
  font-size: 16px;
  text-align: center;
  text-decoration: none;
  color: #333;
  background-color: white;
  border: 2px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15), 0 2px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 鼠标悬停效果 */
.button-3d:hover {
  transform: translateY(-2px);
  box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.2);
}

/* 鼠标点击效果 */
.button-3d:active {
  transform: translateY(1px);
  box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}
</style>
