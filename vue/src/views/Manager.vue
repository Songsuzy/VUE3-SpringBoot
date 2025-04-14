<template>
  <div>
    <!--头部区域开始 -->
    <div style="height: 60px;display: flex;">
      <div style="width:240px;display: flex;align-items: center;padding-left: 20px; background-color: #3a456b">
        <img src="@/assets/imgs/logo.png" style="width: 40px;height: 40px;border-radius: 50%">
        <span style="font-size: 18px;font-weight: bold;color: #f1f1f1;margin-left: 5px">招聘考试系统</span>
      </div>
      <div style="flex: 1;border-bottom: 1px solid #ddd;display: flex;align-items: center;padding-left: 20px">
        <span style="margin-left: 5px; cursor: pointer" @click = "router.push('/manager/home')">首页</span>/<span style="margin-left: 5px;cursor: pointer">{{router.currentRoute.value.meta.name}}</span>
      </div>
      <div style="width: fit-content;display: flex;align-items: center;padding-right: 20px;border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex;align-items: center">
            <img v-if="data.user?.avatar" style="width: 40px; height: 40px; border-radius: 50%" :src="data.user?.avatar" />
            <img v-else style="width: 40px; height: 40px; border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
            <span style="margin-left: 5px">{{data.user?.name}}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!--头部区域结束 -->
    <!--下方区域开始 -->
    <div style="display: flex">
      <!--菜单区域开始 -->
      <div style="width: 240px; box-shadow: 0 0 8px rgba(0, 0, 0, .12);">
        <el-menu router :default-openeds="['1','2']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
            </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">系统公告</el-menu-item>
            <el-menu-item index="/manager/notice" v-if="data.user.role === 'USER'">公告信息</el-menu-item>
            <el-menu-item index="/manager/introduction">旅游攻略</el-menu-item>
            <el-menu-item index="/manager/category">攻略分类</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">普通用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <!--菜单区域结束 -->
      <!--数据渲染区域开始 -->
      <div style="flex: 1;width: 0;padding: 10px;background-color: #f2f4ff">
        <RouterView @updateUser="updateUser"/>
      </div>
      <!--数据渲染区域结束 -->
    </div>
    <!--下方区域结束 -->
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}")
})
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

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("code_user") || '{}')
}
// if (!data.user?.id) {
//   location.href = '/login'
// }
// 根据浏览器缓存在前端进行验证拦截，不够安全，可以通过捏造浏览器缓存数据进行访问
</script>
<style>
.el-menu {
  background-color: #3a456b;
  border: none;
}
.el-sub-menu__title {
  color: #ddd;
  background-color: #3a456b;

}
.el-menu-item {
  color: #ddd;
  height: 50px;
}
.el-menu .is-active {
  background-color: #537bee;
  color: #fff;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}
.el-menu-item:not(.is-active):hover{
  background-color: #7a9fff;
  color: #333;
}
.el-tooltip__trigger {
  outline: none;
}
.el-dropdown {
  cursor: pointer;
}
.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}
</style>