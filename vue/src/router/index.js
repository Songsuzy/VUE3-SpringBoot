import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/',redirect:'/manager/home'},
    {path: '/manager',component: () =>import('../views/Manager.vue'),
    children: [
      {path: 'home', meta: { name: '主页'},component: () => import('../views/Home.vue'),},
      {path: 'admin', meta: { name: '管理员信息'}, component: () =>import('../views/Admin.vue'),},
      {path: 'user', meta: { name: '普通用户信息'}, component: () =>import('../views/User.vue'),},
      {path: 'person', meta: { name: '个人中心'}, component: () =>import('../views/Person.vue'),},
      {path: 'updatePassword', meta: { name: '密码管理'}, component: () =>import('../views/UpdatePassword.vue'),},
      {path: 'notice', meta: { name: '系统公告'}, component: () =>import('../views/Notice.vue'),},
      {path: 'introduction', meta: { name: '攻略页面'}, component: () =>import('../views/Introduction.vue'),},
      {path: 'category', meta: { name: '攻略分类'}, component: () =>import('../views/Category.vue'),},
      {path: 'apply', meta: { name: '请假申请'}, component: () =>import('../views/Apply.vue'),},
      {path: 'book', meta: { name: '图书管理'}, component: () =>import('../views/Book.vue'),},
      {path: 'record', meta: { name: '借阅记录'}, component: () =>import('../views/Record.vue'),}
    ]
    },
    {path: '/front/home',component: import('../views/Front.vue'),},
    {path: '/notfound',component: import('../views/404.vue'),},
    {path: '/login',component: import('../views/Login.vue'),},
    {path: '/register',component: import('../views/Register.vue'),},
    {path: '/:pathMatch(.*)',redirect:'/notfound'}
  ],
})

export default router
