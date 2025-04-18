
<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 260px;margin-right: 5px" v-model="data.name" placeholder="请输入标题查询" :prefix-icon="Search" clearable ></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="info" @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px; display: flex;align-items: center" v-if="data.user.role === 'ADMIN'">
      <el-button type="primary"  @click="handleAdd">新增书籍</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{ color:'#333',backgroundColor:'#eaf4ff'}">
        <el-table-column label="图书封面" width="80px">
          <template #default="scope">
            <el-image :src="scope.row.img" :preview-src-list="[scope.row.img]"
                      :preview-teleported="true" style="width: 40px;height: 40px; border-radius: 5%; display: block" v-if="scope.row.img"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="图书名称"/>
        <el-table-column prop="price" label="图书价格"/>
        <el-table-column prop="author" label="作者"/>
        <el-table-column prop="num" label="总数" />
        <el-table-column label="操作" width="100">
          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
          <template #default="scope" v-else>
            <el-button type="primary" @click="borrow(scope.row)">借阅</el-button>
          </template>
        </el-table-column>
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
    <el-dialog v-model="data.formVisible" title="请假信息" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="img" label="图书封面">
          <el-upload
              action="http://localhost:9999/files/upload"
              :on-success="handleFileSuccess"
              :headers="{token: data.user.token}"
              list-type="picture"
          >
            <el-button type="primary">{{ data.form.avatar ? '更换封面' : '上传封面' }}</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="图书名称">
          <el-input  v-model="data.form.name" autocomplete="off" placeholder="请输入图书名称" />
        </el-form-item>
        <el-form-item prop="price" label="图书价格">
          <el-input  v-model="data.form.price" autocomplete="off" placeholder="请输入图书价格" />
        </el-form-item>
        <el-form-item prop="author" label="作者">
          <el-input  v-model="data.form.author" autocomplete="off" placeholder="请输入图书作者" />
        </el-form-item>
        <el-form-item prop="num" label="数量">
          <el-input  v-model="data.form.num" autocomplete="off" placeholder="请输入图书剩余总数" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">
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
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  form: {},
  formVisible: false,
  name: null,
  tableData: [],
  rules: {
    img: [
      { required: true, message: '请上传图书封面', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请填写图书名称', trigger: 'blur' }
    ],
    price: [
      { required: true, message: '请填写图书价格', trigger: 'blur' }
    ],
    author: [
      { required: true, message: '请填写图书作者', trigger: 'blur' }
    ],
    num: [
      { required: true, message: '请填写图书总数', trigger: 'blur' },
      { pattern: /^\d+$/, message: '只能输入数字', trigger: 'blur' }
    ]
  }
})
const formRef = ref()

const load = () => {
  request.get('book/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
const reset = () => {
  data.title = null
  load()
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('book/add',data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('新增请假流程成功，请等待管理员审核')
          data.formVisible = false
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/book/update',data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('操作成功')
          data.formVisible = false
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  data.form.id? update():add()
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
        request.delete('/book/delete/' + id).then(res => {
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
const handleFileSuccess = (res) =>{
  data.form.img = res.data
}

const borrow = (row) => {
  request.post('record/add',{
    userId: data.user.id,
    bookId: row.id
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功，待管理员审核')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>
