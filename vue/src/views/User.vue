
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
        <el-button type="primary"  @click="handleAdd">新 增</el-button>
        <el-button type="danger"  @click="deleteBatch">批量删除</el-button>
        <el-upload
            style="display: inline-block; margin-right: 5px;margin-left: 5px"
            plain
            action="http://localhost:9999/user/importData"
            :show-file-list="false"
            :headers="data.uploadHeaders"
            :on-success="handleImportSuccess"
        >
          <el-button type="success">批量导入</el-button>
        </el-upload>
        <el-button type="info"  @click="exportData">批量导出</el-button>
      </div>
      <div class="card" style="margin-bottom: 5px">
        <el-table :data="data.tableData" style="width: 100%"
                  @selection-change="handleSelectionChange"
                  :header-cell-style="{ color:'#333',backgroundColor:'#eaf4ff'}">
          <el-table-column type="selection" width="55" />
          <el-table-column label="头像" width="80px">
            <template #default="scope">
              <el-image :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"
                        :preview-teleported="true" style="width: 40px;height: 40px; border-radius: 50%; display: block" v-if="scope.row.avatar"/>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="账号"/>
          <el-table-column prop="name" label="名称"/>
          <el-table-column prop="phone" label="电话"  />
          <el-table-column prop="email" label="邮箱"  />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
              <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
            </template>
          </el-table-column>
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
      <el-dialog v-model="data.formVisible" title="普通用户信息" width="500" destroy-on-close @close="data.onEdit = false">
        <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
          <el-form-item prop="username" label="账号">
            <el-input  v-model="data.form.username" autocomplete="off" placeholder="请输入账号" :readonly="data.onEdit"/>
          </el-form-item>
          <el-form-item prop="name" label="名称">
            <el-input  v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item prop="phone" label="电话">
            <el-input  v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input  v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
          </el-form-item>
          <el-form-item prop="avatar" label="头像">
            <el-upload
            action="http://localhost:9999/files/upload"
            :on-success="handleFileSuccess"
            :headers="{token: data.user.token}"
            list-type="picture"
            >
              <el-button type="primary">{{ data.form.avatar ? '更换头像' : '上传头像' }}</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="data.formVisible = false; data.onEdit = false">取 消</el-button>
            <el-button type="primary" @click="save()">
           保 存
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>

</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import axios from "axios";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  uploadHeaders: {"token" :JSON.parse(localStorage.getItem('code_user') || '{}').token},
  name: null,
  username: null,
  phone: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  formVisible:false,
  onEdit: false,
  form: {},
  rules: {
    username: [
      { required: true, message: '请填写账号', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请填写名称', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请填写电话', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请填写邮箱', trigger: 'blur' }
    ]
  },
  rows:[],
  ids:[]
})

const formRef = ref()

const load =() =>{
  request.get('/user/selectPage', {
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
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/user/add',data.form).then(res => {
        if (res.code === '200'){
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })

}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  data.onEdit = true
}

const update = () =>{
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/user/update',data.form).then(res => {
        if (res.code === '200'){
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm(
      '删除后无法恢复，确认删除吗?',
      '删除确认',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        request.delete('/user/delete/' + id).then(res => {
          if (res.code === '200'){
            ElMessage.success('删除成功')
            load()
          }else {
            ElMessage.error(res.msg)
          }
        })
      })
      .catch(err => {})
}
const handleSelectionChange = (rows) =>{
  // console.log(rows)
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}
const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的数据！')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/user/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}
const exportData =() => {
  let idsStr = data.ids.join(",");
  let url = `http://localhost:9999/user/export?username=${data.username === null ? '' :data.username}`
  +`&name=${data.name === null?'':data.name}`
  +`&ids=${idsStr}`
  +`&token=${data.user.token}`
  window.open(url)
}

const handleImportSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量导入数据成功')
    load()
  } else {
    ElMessage.error(res.msg)
  }
}
const handleFileSuccess = (res) =>{
  data.form.avatar = res.data
}
</script>
