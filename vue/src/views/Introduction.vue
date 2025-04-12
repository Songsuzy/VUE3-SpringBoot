<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 260px;margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search" clearable ></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="info" @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px; display: flex;align-items: center">
      <el-button type="primary"  @click="handleAdd">新 增</el-button>
<!--      <el-button type="danger"  @click="deleteBatch">批量删除</el-button>-->
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{ color:'#333',backgroundColor:'#eaf4ff'}">
        <el-table-column label="图片" width="80px">
          <template #default="scope">
            <el-image :src="scope.row.img" :preview-src-list="[scope.row.img]"
                      :preview-teleported="true" style="width: 50px;height: 50px; border-radius: 5%; display: block" v-if="scope.row.img"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="攻略标题"/>
        <el-table-column prop="content" label="攻略内容">
          <template v-slot="scope">
            <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"  />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
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
    <el-dialog v-model="data.formVisible" title="攻略信息" width="60%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="img" label="图片">
          <el-upload
              action="http://localhost:9999/files/upload"
              :on-success="handleFileSuccess"
              :headers="{token: data.user.token}"
              list-type="picture"
          >
            <el-button type="primary">上传攻略图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="title" label="攻略标题">
          <el-input  v-model="data.form.title" autocomplete="off" placeholder="请输入攻略标题" />
        </el-form-item>
        <el-form-item prop="content" label="攻略内容">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
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
    <el-dialog title="攻略信息" v-model="data.viewVisible" width="60%" destroy-on-close>
      <div v-html="data.content" style="padding: 0 20px"></div>
    </el-dialog>
  </div>
</template>
<script setup>

import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  title: null,
  form: {},
  formVisible: false,
  tableData: [],
  content: null,
  viewVisible: false,
  rules: {
    title: [
      { required: true, message: '请填写公告标题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请填写公告内容', trigger: 'blur' }
    ]
  }
})

/* wangEditor5 初始化开始 */
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: 'http://localhost:9999/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file'  // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */
const formRef = ref()

const load = () => {
  request.get('introduction/selectPage',{
    params: {
      pageNum : data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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

const reset = () => {
  data.total = null
  load()
}

load()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleFileSuccess = (res) => {
  data.form.img = res.data
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
        request.delete('/introduction/delete/' + id).then(res => {
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

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/introduction/add',data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('新增成功')
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
      request.put('introduction/update',data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('更新成功')
          data.formVisible = false
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const viewContent = (content) => {
  data.content = content
  data.viewVisible = true
}
const save = () => {
  data.form.id? update() : add()
}
</script>