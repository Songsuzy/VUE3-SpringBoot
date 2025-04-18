<template>
  <div class="bg">
    <el-menu
      :default-active="1"
      class=""
      mode="horizontal"
    >
      <el-menu-item index="1">系统首页</el-menu-item>
      <el-menu-item @click="logout">退出</el-menu-item>
    </el-menu>

    <div style="margin-bottom: 20px">
      <el-carousel height="350px">
<!--        <el-carousel-item v-for="item in data.carouselData" :key="item">-->
<!--          <img :src="item" alt="" style="height: 350px; width: 100%">-->
<!--        </el-carousel-item>-->
<!--        静态-->
        <el-carousel-item v-for="item in data.introductionData" :key="item">
          <img :src="item.img" alt="" style="height: 350px; width: 100%; cursor: pointer" @click="navTo('/front/introductionDetail?id='+ item.id)">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="width: 80%; margin: 20px auto">
      <div style="font-size: 20px; border-left: 5px solid #2fbd67;padding-left: 10px;height: 30px;line-height: 30px">旅游攻略</div>
      <div style="margin-top: 20px">
        <el-row gutter="20">
          <el-col :span="6" v-for="item in data.introductionData" style="margin-bottom: 20px">
            <img  :src="item.img" style="width: 100%;height: 200px; border-radius: 10px; cursor: pointer" @click="navTo('/front/introductionDetail?id='+ item.id)">
            <div style="font-size: 17px;font-weight: bold;margin-top: 7px; cursor: pointer" @click="navTo('/front/introductionDetail?id='+ item.id)">{{item.title}}</div>
            <div style="display: flex;align-items: center;margin-top: 10px;grid-gap: 10px">
              <img :src="item.userAvatar" style="width: 30px;height: 30px;border-radius: 50%">
              <div style="font-size: 15px">{{item.userName}} </div>
              <div style="color: #666">{{item.time}}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div style="width: 80%; margin: 20px auto">
      <div style="font-size: 20px; border-left: 5px solid #2fbd67;padding-left: 10px;height: 30px;line-height: 30px">旅游攻略</div>
        <div style="display: flex; height: 250px;margin-top: 20px;grid-gap: 20px" v-for="item in data.introductionData">
          <div style="flex: 1">
            <img :src="item.img" style="width: 100%;height: 220px;border-radius: 10px; cursor: pointer" @click="navTo('/front/introductionDetail?id='+ item.id)">
          </div>
          <div style="flex: 3">
            <div style="font-size: 20px;font-weight: bold; cursor: pointer" @click="navTo('/front/introductionDetail?id='+ item.id)">{{item.title}}</div>
            <div class="line4" style="margin-top: 10px;font-size: 15px;color: #666666;text-align: justify;line-height: 25px;height: 125px">{{item.description}}</div>
            <div style="display: flex;align-items: center;margin-top: 10px;grid-gap: 10px">
              <img :src="item.userAvatar" style="width: 30px;height: 30px;border-radius: 50%">
              <div style="font-size: 15px">{{item.userName}} </div>
              <div style="color: #666">{{item.time}}</div>
            </div>
          </div>
        </div>
    </div>

  </div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import lun1 from "@/assets/imgs/lun_1.jpg"
import lun2 from "@/assets/imgs/lun_2.jpg"
import lun3 from "@/assets/imgs/lun_3.jpg"

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  introductionData:[],
  carouselData: [lun1, lun2, lun3]
})
const loadIntroduction = () => {
  request.get('/introduction/selectAll').then(res => {
    if (res.code === '200') {
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadIntroduction()
const logout = () => {
  ElMessageBox.confirm(
      '您确认要退出登录吗?',
      '退出登录',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        ElMessage.success('成功退出')
        setInterval(()=> {
          localStorage.removeItem('code_user')
          location.href = '/login'
        }, 200 )
      })
}
const navTo = (url) => {
  location.href = url
}
</script>
<style>
.line4 {
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
  overflow: hidden;
}
.bg {
  background-color: lavenderblush;
  min-height: 100vh;  /* 改用视口单位确保撑满屏幕 */
  width: 100%;
  margin: 0;
  padding: 0;
}
</style>