<template>
<el-container>
  <!-- // 头部 -->
  <el-header class="header">
    <p class="title">欢迎您</p>
  </el-header>
  <el-container>
    <!-- // 侧边栏 -->
    <!-- <el-aside width="200px">Asasdasdaide</el-aside> -->
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
              </div>
            </div>
          </template>
        </el-calendar>
        <div class="check-in-button-container">
            <el-button type="primary" @click="signIn" round>签到</el-button>
        </div>
        <div class="tip">
          每天签到领积分,连续签到积分更多
        </div>
      </div>
      <div class="main-right">
          <div class="calendar">
            <div class="calendar-col">
              <p class="item1">您已通过签到累计获得了</p>
              <br>
              <!-- <img src="~@/assets/imgs/sign/treasure-trove.png" alt="">
               -->
               <div class="myimgs"> 
                  <p class="item">+{{accumulatedSignCount}}积分</p>
               </div>
              </div>
          </div>
      </div>
    </el-main>

    <!-- 签到弹窗 -->
    <div class="tost" v-if="showDialog">
      <div class="tost-content">
        <!-- 签到头部 -->
        <div class="signheader">
          <img src="~@/assets/imgs/SignInHead.png" alt="签到头" style="width: 360rpx;">
        </div>
        <!-- 签到盒子 -->
        <div class="signbox">
          <br><br><br>
          <div class="title">
            当前连续签到第{{ continuousSignCount }}天，获得{{pointsCount}}积分
          </div>
          <div class="tip">
            连续签到第7天后每天获得+7积分
          </div>
          <!-- 签到金币 -->
          <div class="signGold">
            <div v-for="(item, index) in week" :key="index" class="gold">
              <div v-if="continuousSignCount > index" class="signed">
                  <p class="item">+{{item}}</p>
              </div>
              <div v-else class="notsigned">
                <p class="item">+{{item}}</p>
              </div>
              <p class="myday">第{{ index + 1 }}天</p>
            </div>
          </div>
        </div>
        <!-- 关闭按钮 -->
        <div class="btn">
          <img src="~@/assets/imgs/close.png" alt="关闭"
           style="width: 60px;margin :20px 0 0 0; "
            @click="showDialog = false">
        </div>
      </div>
    </div>


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
    // this.getSignCount(date)
    // 获得签到信息
    this.getSignInfo(date)
    // 获得连续签到次数、累计签到积分
    this.getSignCountInfo(date)
  },
  data(){
    return{
      calendarData: {},
      // 签到次数
      signCount: 0,
      // 连续签到次数
      continuousSignCount:0,
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
      //签到奖励梯队
      week: [1, 2, 3, 4, 5, 6, 7],

    }
  },
  // 监听
  watch: {
    calendarDate(val, oldVal){
      if(val != '' && oldVal != ''){
        if(val.getMonth() != oldVal.getMonth()){
          const date = formatDate(val)
          // 刷新
          // this.getSignCount(date)
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
          this.continuousSignCount = resp.data.count
          this.pointsCount = resp.data.pointsCount
          this.isSigned = resp.data.isSigned
          this.nextPointsCount = resp.data.nextPointsCount
          this.accumulatedSignCount = resp.data.accumulatedSignCount
          // 刷新签到信息
          this.getSignInfo(date)
          // this.getSignCount(date)
        }
        //弹窗显示
        this.showDialog = true
      })
      
    },
    //获得累计签到积分
    getaccumulatedSignCount(){
      signIn.getaccumulatedSignCount().then(resp => {
        if (resp.code === 200) {
          this.accumulatedSignCount = resp.data
        }
      })
    },
    // getSignCount(data){
    //   console.log(data);
    //   signIn.getSignCount(data).then(resp => {
    //     if (resp.code === 200) {
    //       this.signCount = resp.data
    //         this.$notify   ({
    //         title: 'Tips',
    //         message: '日期-'+resp.message,
    //         type: 'success',
    //         duration: 2000
    //       })
    //     }
    //   })
    // },
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
    getSignCountInfo(data){
      signIn.getSignCountInfo(data).then(resp => {
        if (resp.code === 200) {
          this.continuousSignCount = resp.data.continuousSignCount
          this.accumulatedSignCount = resp.data.accumulatedSignCount
        }
      })
    }

  }
}
</script>

<style scoped lang="scss">

 .title {
    font-weight: bold;
    text-align: center;
    font-size: 30px;
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
    .calendar{
      display: flex;       /* 使.calendar成为flex容器 */
      justify-content: center; /* 水平居中.calendar-col */
    }
    .calendar-col{
      display: flex;       /* 使.calendar-col成为flex容器 */
      align-items: center; /* 垂直居中内容 */
      flex-direction: column; /* 设置为垂直方向排列子元素 */
    }
    .item1{
      color: #f86707;
      font-weight: bold;
    }
    .myimgs{
      
      .item{
          color: #f8dc07;
          font-weight: bold;
          font-size: 30px;
          transform: translateY(80px); /* 向上偏移自身的50%，实现中上1/3的效果 */
        }
        
        background: url('~@/assets/imgs/sign/treasure-trove.png');
        background-size: 100% 100%;
        width:300px;
        height:300px;
        display: flex;        /* 使用 Flexbox 布局 */
        align-items: center;  /* 垂直居中 */
        justify-content: center; /* 水平居中，如果需要的话 */
    }
  }
}
.is-passed {
  max-width: 100%; 
  max-height: 100%;
}

.dialog-content {
  display: flex;
}

.tip{
			margin-top: 20rpx;
			font-weight: 500;
			font-size: 24rpx;
			color: #AAAAAA;
			line-height: 28rpx;
			text-align: center; 
		}
 .tost{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: 999;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;

	.signheader{
		position: relative;
		bottom: -100px;
		text-align: center;
	}
  
	.signbox{
		width: 600px;
    height: 430px;
		background: #FFFFFF;
		border-radius: 20rpx;
		padding: 40rpx 20rpx;
    border-radius: 8px; /* 圆角半径大小，可以根据需要调整 */

		.title{
			font-weight: 600;
			font-size: 30rpx;
			color: #333333;
			line-height: 33rpx;
			text-align: center;
			margin-top: 20rpx;
			text{
				font-weight: bold;
				font-size: 40rpx;
				color: #F93A59;
				line-height: 47rpx;
			}
		}
		
		.signGold{
      width: 100%;
			margin-top: 20rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-wrap: wrap;
			.gold{
				margin: 20px 20px 0 20px ;
				view{
					font-weight: 500;
					font-size: 24rpx;
					color: #999999;
					line-height: 28rpx;
					text-align: center;
				}
        .myday{
          text-align: center;
        }
			}
      .notsigned{
        .item{
          color: #e67606;
          font-size: 18px;
          transform: translateY(-100%); /* 向上偏移自身的50%，实现中上1/3的效果 */
        }
        background: url('~@/assets/imgs/sign/notsigned.png');
        background-size: 100% 100%;
        width:100px;
        height:100px;
        display: flex;        /* 使用 Flexbox 布局 */
        align-items: center;  /* 垂直居中 */
        justify-content: center; /* 水平居中，如果需要的话 */

      }
      .signed{
        .item{
          color: #e61106;
          font-weight: bold;
          font-size: 24px;
          transform: translateY(-80%); /* 向上偏移自身的50%，实现中上1/3的效果 */
        }
        background: url('~@/assets/imgs/sign/signed.png');
        background-size: 100% 100%;
        width:100px;
        height:100px;
        display: flex;        /* 使用 Flexbox 布局 */
        align-items: center;  /* 垂直居中 */
        justify-content: center; /* 水平居中，如果需要的话 */
      }
		}
	}
	
	.btn{
		display: flex;
		justify-content: center;
		// margin-top: 40rpx;
	}
	
}

</style>
