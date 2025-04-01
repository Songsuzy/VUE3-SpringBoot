
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
        <el-button type="primary" plain @click="handleAdd">新 增</el-button>
        <el-button type="danger" plain>批量删除</el-button>
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
      <el-dialog v-model="data.formVisible" title="管理员信息" width="500">
        <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
          <el-form-item prop="username" label="账号">
            <el-input  v-model="data.form.username" autocomplete="off" />
          </el-form-item>
          <el-form-item prop="name" label="名称">
            <el-input  v-model="data.form.name" autocomplete="off" />
          </el-form-item>
          <el-form-item prop="phone" label="电话">
            <el-input  v-model="data.form.phone" autocomplete="off" />
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input  v-model="data.form.email" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="data.formVisible = false">取 消</el-button>
            <el-button type="primary" @click="add">
           保 存
            </el-button>
          </div>
        </template>
      </el-dialog>
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
  tableData: [],
  formVisible:false,
  form: {},
  rules: {
    username: [
      { required: true, message: '请填写账号', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请填写名称', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请填写手机', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请填写邮箱', trigger: 'blur' }
    ]
  }
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
const handleAdd =() => {
  data.formVisible = true
  data.form = {}
}
const add = () => {
  request.post('/admin/add',data.form).then(res => {
    if (res.code === '200'){
      data.formVisible = false
      ElMessage.success('新增成功')
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}

</script>
