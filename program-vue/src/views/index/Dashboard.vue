<template>
<el-container>
  <!-- // 头部 -->
  <el-header class="header">
    <p class="title">欢迎您</p>
    
  </el-header>
  <el-container>
    <!-- // 侧边栏 -->
    <!-- <el-aside width="200px">Aside</el-aside> -->
    <!-- // 主体 -->
    <el-main class="main">
      <div class="main-left">
         <el-calendar class="calendar" v-model="calendarDate">
          <template #dateCell="{ data }">
            <div class="col">
              <p>{{ data.day.split('-').slice(2).join('-') }}</p>
              <div class="is-passed" >
                <!-- 在 calendarData 中找到对应日期 -->
                <span v-if="calendarData[data.day]">
                  <img src="~@/assets/imgs/Selected.svg"  
                      style="max-width: 60%; max-height:60%;" alt="" />
                </span>
                <!-- <span v-else>unpass</span> -->
              </div>
            </div>
          </template>
        </el-calendar>
        <div class="sign-count">
          <p>本月签到次数：{{signCount}} 次</p>
          <p>本月连续签到次数：{{ContinuousCignCount}} 次</p>
        </div>
        <div class="check-in-button-container">
            <el-button type="primary" @click="signIn" round>签到</el-button>
          </div>
      </div>
      <div class="main-right">
          <!-- 签到按钮 -->
      </div>
    </el-main>

     <!-- 签到弹窗 -->
    <el-dialog title="签到详情" :visible.sync="showDialog" width="30%" >
      <div class="main">
        <div class="main-left"> 
          <img v-if="!isSigned" src="~@/assets/imgs/signSuccessful.png" alt="签到图标" class="check-in-icon">
          <img v-else src="~@/assets/imgs/sepeatSign.png" alt="签到图标" class="check-in-icon">
        </div >
        <div class="main-right">
          <p>累计签到积分:{{accumulatedSignCount}}</p>
          <p >连续签到次数: {{ ContinuousCignCount }}</p>
          <p>本次签到积分: {{ pointsCount }}</p>
          <p>下一次签到积分: {{ nextPointsCount }}</p>
        </div>
        
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showDialog = false">关闭</el-button>
      </span>
    </el-dialog>
  </el-container>
</el-container>
</template>

<script>
import signIn from '@/api/signIn'
import { formatDate } from "@/utils/DateUtil"
export default {
  name: 'Dashboard',
  created () {

    // 获得当前日期 格式化yyyy-MM-dd
    const date = formatDate(new Date())
    // 获得签到次数
    this.getSignCount(date)

    // 获得签到信息
    this.getSignInfo(date)
    // 获得连续签到次数
    this.getContinuousSignCount(date)
  },
  data(){
    return{
      calendarData: {},
      // 签到次数
      signCount: 0,
      // 连续签到次数
      ContinuousCignCount:0,
      // 签到日期,初始为当前日期
      calendarDate: new Date(),
      // 签到弹窗
      showDialog: false,
      // 签到积分
      pointsCount: 0,
      // 下一次签到积分
      nextPointsCount: 0,
      // 今天是否签到
      isSigned: false,
      //累计签到积分
      accumulatedSignCount: 0,
      
    }
  },
  // 监听
  watch: {
    calendarDate(val, oldVal){
      if(val != '' && oldVal != ''){
        if(val.getMonth() != oldVal.getMonth()){
          const date = formatDate(val)
          // 刷新
          this.getSignCount(date)
          this.getSignInfo(date)
        }
      }
    }
  },
  methods: {
    signIn(){
      // 2024-08-16
      const date = formatDate(new Date())
      const data = {
        "date": date
      }
      
      signIn.signIn(data).then(resp => {
        
        if (resp.code === 200) {
          this.ContinuousCignCount = resp.data.count
          this.pointsCount = resp.data.pointsCount
          this.isSigned = resp.data.isSigned
          this.nextPointsCount = resp.data.nextPointsCount
          this.accumulatedSignCount = resp.data.accumulatedSignCount
          // 刷新签到信息
          this.getSignInfo(date)
          this.getSignCount(date)
          this.$message({  
            message: '恭喜你，这是一条成功消息,已连续签到'+this.ContinuousCignCount+'天',  
            type: 'success'  
          });
        }
        //弹窗显示
        this.showDialog = true
        debugger
        console.log(this.isSigned)
      })
      
    },
    getSignCount(data){
      console.log(data);
      signIn.getSignCount(data).then(resp => {
        if (resp.code === 200) {
          this.signCount = resp.data
            this.$notify   ({
            title: 'Tips',
            message: '日期-'+resp.message,
            type: 'success',
            duration: 2000
          })
        }
      })
    },
    getSignInfo(data){
      signIn.getSignInfo(data).then(resp => {
        if (resp.code === 200) {
          this.calendarData = resp.data
        }
      })
    },
    handleMonthChange(value){
      debugger
      
      console.log("calendarDate"+calendarDate);
      console.log(value);
    },
    getContinuousSignCount(data){
      signIn.getContinuousSignCount(data).then(resp => {
        if (resp.code === 200) {
          this.ContinuousCignCount = resp.data
        }
      })
    }

  }
}
</script>

<style scoped lang="scss">

 .title {
    text-align: center;
    font-size: 25px;
    margin: 0 0 0 0;
  }

.header{
  // height: 60% !important;
}

::v-deep .el-calendar-table .el-calendar-day {
  height: 3em;
  width: 100%;
  text-align: left;
}
.main{
  display: flex;
  .main-left{
    width: 50%;
    .check-in-button-container {
      display: flex;
      justify-content: center;
      align-items: center;
      /* 如果需要调整按钮的样式，可以在下面添加 */
      .el-button--primary {
        padding: 10px 20px;
        font-size: 20px;
        width: 25%;
      }
    }
    .col{
      display: flex;
    }
  }
  .main-right{
    width: 50%;
    // border : solid   1px   #333;

  }
}
.is-passed {
  max-width: 100%; 
  max-height: 100%;
}

.dialog-content {
  display: flex;
  // flex-direction: column;
  // align-items: center;
  // // padding-top: 20px;
  // height: 50%;
  // width: 100%;

}

.check-in-icon {
  // width: 50px; /* 调整图标大小 */
  // margin-bottom: 10px;
  // margin: 0 auto; /* 横向居中 */
}
</style>
