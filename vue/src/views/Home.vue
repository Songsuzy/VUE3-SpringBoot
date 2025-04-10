<template>
<div>
  <div class="card">Suzy毕业顺利</div>
  <div class="card" style="margin-top: 10px;width: 50%">
    <div style="margin-bottom: 20px;font-size: 18px">系统公告</div>
    <el-timeline style="max-width: 600px">
      <el-timeline-item :timestamp="item.time" color="#0bbd87" placement="top" v-for="item in data.noticeData">
        <h4>{{item.title}}</h4>
        <p>{{item.content}}</p>
      </el-timeline-item>
    </el-timeline>
  </div>
</div>
</template>
//也可以使用折叠面板手风琴效果来循环展示
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  noticeData: []
})
const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
      if (data.noticeData.length >3)
      { data.noticeData = data.noticeData.slice(0,4) }
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
</script>
<style scoped>

</style>