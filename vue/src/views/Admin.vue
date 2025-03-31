
<template>
    <div>
      <div class="card" style="margin-bottom: 5px">
        <el-input style="width: 260px;margin-right: 5px" v-model="data.username" placeholder="请输入账号" :prefix-icon="Search" clearable ></el-input>
        <el-input style="width: 260px;margin-right: 5px" v-model="data.name" placeholder="请输入名称" :prefix-icon="Search" clearable ></el-input>
        <el-input style="width: 260px;margin-right: 5px" v-model="data.phone" placeholder="请输入电话" :prefix-icon="Search" clearable ></el-input>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button type="info" @click="reset">重 置</el-button>
      </div>
      <div class="card" style="margin-bottom: 5px; display: flex;align-items: center">
        <el-button type="danger" plain>批量删除</el-button>
        <el-button type="primary" plain>新 增</el-button>
        <el-button type="warning" plain>批量导入</el-button>
        <el-button type="info" plain>批量导出</el-button>
      </div>
      <div class="card" style="margin-bottom: 5px">
        <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color:'#333',backgroundColor:'#eaf4ff'}">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="username" label="账号"/>
          <el-table-column prop="name" label="名称"/>
          <el-table-column prop="phone" label="电话"  />
          <el-table-column prop="email" label="邮箱"  />
<!--          <el-table-column prop="address" label="地址"/>-->
        </el-table>
      </div>
      <div class="card" style="margin-bottom: 5px">
        <el-pagination
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :page-sizes="[5, 10, 20]"
            :total="data.total"
            @current-change="load"
            @size-change="load"
        />
      </div>
    </div>

</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive} from "vue";
import axios from "axios";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  name: null,
  username: null,
  phone: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: []
})

const load =() =>{
  request.get('/admin/selectPage', {
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name,
      username: data.username,
      phone: data.phone
    }
      }).then(res => {
        if (res.code === '200'){
          console.log(res.data)
          data.total = res.data.total
          data.tableData = res.data.list
        }else {
          ElMessage.error(res.msg)
        }
  })
}
load()

const reset = () => {
  data.name = null
  data.username = null
  data.phone = null
  load()
}

</script>
