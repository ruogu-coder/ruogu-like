import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/main',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: 'blog',
        name: 'BlogList',
        component: () => import('@/views/Blog/List.vue')
      },
      {
        path: 'blog/:id',
        name: 'BlogDetail',
        component: () => import('@/views/Blog/Detail.vue'),
        props: true
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/User/Profile.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'password',
        name: 'ChangePassword',
        component: () => import('@/views/User/Password.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'users',
        name: 'UserList',
        component: () => import('@/views/User/List.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/User/Login.vue'),
    meta: { guestOnly: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/User/Register.vue'),
    meta: { guestOnly: true }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 导航守卫
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('ruogu-like-token');
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
  const isAdmin = userInfo.role === 'ADMIN';
  
  // 路由需要认证
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } });
    return;
  }
  
  // 路由需要管理员权限
  if (to.meta.requiresAdmin && !isAdmin) {
    next({ name: 'Home' });
    return;
  }
  
  // 路由仅供游客访问（登录、注册）
  if (to.meta.guestOnly && token) {
    next({ name: 'Home' });
    return;
  }
  
  next();
});

export default router; 