<template>
  <a-layout class="layout">
    <a-layout-header class="header">
      <div class="header-content">
        <div class="logo" @click="router.push('/')">
          <HeartFilled class="logo-icon" />
          <span class="logo-text">亿级点赞</span>
        </div>
        
        <div class="mobile-trigger" @click="collapsed = !collapsed">
          <MenuOutlined v-if="!collapsed" />
          <CloseOutlined v-else />
        </div>
        
        <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="horizontal"
          class="desktop-menu"
        >
          <a-menu-item key="home">
            <router-link to="/">首页</router-link>
          </a-menu-item>
          <a-menu-item key="blog">
            <router-link to="/blog">博客</router-link>
          </a-menu-item>
          
          <!-- 仅管理员可见菜单 -->
          <template v-if="userStore.isAdmin">
            <a-menu-item key="users">
              <router-link to="/users">用户管理</router-link>
            </a-menu-item>
          </template>
          
          <div class="flex-spacer"></div>
          
          <!-- 游客菜单 -->
          <template v-if="!userStore.isLoggedIn">
            <a-menu-item key="login">
              <router-link to="/login">
                <LoginOutlined />
                <span>登录</span>
              </router-link>
            </a-menu-item>
            <a-menu-item key="register">
              <router-link to="/register">
                <UserAddOutlined />
                <span>注册</span>
              </router-link>
            </a-menu-item>
          </template>
          
          <!-- 已登录用户菜单 -->
          <template v-else>
            <a-sub-menu key="user">
              <template #title>
                <UserOutlined />
                <span>{{ userStore.userInfo.userName || '用户' }}</span>
              </template>
              <a-menu-item key="profile">
                <router-link to="/profile">个人资料</router-link>
              </a-menu-item>
              <a-menu-item key="password">
                <router-link to="/password">修改密码</router-link>
              </a-menu-item>
              <a-menu-item key="logout" @click="logout">
                退出登录
              </a-menu-item>
            </a-sub-menu>
          </template>
        </a-menu>
      </div>
      
      <!-- 移动端侧边菜单 -->
      <div class="mobile-menu-wrapper" :class="{ active: collapsed }">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="inline"
          class="mobile-menu"
        >
          <a-menu-item key="home" @click="goToRoute('/')">
            <HomeOutlined />
            <span>首页</span>
          </a-menu-item>
          <a-menu-item key="blog" @click="goToRoute('/blog')">
            <ReadOutlined />
            <span>博客</span>
          </a-menu-item>
          
          <!-- 管理员菜单 -->
          <template v-if="userStore.isAdmin">
            <a-menu-item key="users" @click="goToRoute('/users')">
              <TeamOutlined />
              <span>用户管理</span>
            </a-menu-item>
          </template>
          
          <a-divider style="margin: 8px 0; background-color: #303030;" />
          
          <!-- 游客菜单 -->
          <template v-if="!userStore.isLoggedIn">
            <a-menu-item key="login" @click="goToRoute('/login')">
              <LoginOutlined />
              <span>登录</span>
            </a-menu-item>
            <a-menu-item key="register" @click="goToRoute('/register')">
              <UserAddOutlined />
              <span>注册</span>
            </a-menu-item>
          </template>
          
          <!-- 已登录用户菜单 -->
          <template v-else>
            <a-sub-menu key="user">
              <template #title>
                <UserOutlined />
                <span>{{ userStore.userInfo.userName || '用户' }}</span>
              </template>
              <a-menu-item key="profile" @click="goToRoute('/profile')">
                个人资料
              </a-menu-item>
              <a-menu-item key="password" @click="goToRoute('/password')">
                修改密码
              </a-menu-item>
              <a-menu-item key="logout" @click="handleLogout">
                退出登录
              </a-menu-item>
            </a-sub-menu>
          </template>
        </a-menu>
      </div>
    </a-layout-header>
    
    <!-- 遮罩层 -->
    <div 
      v-if="collapsed" 
      class="mobile-menu-overlay" 
      @click="collapsed = false"
    ></div>
    
    <a-layout-content>
      <div class="content-wrapper">
        <router-view></router-view>
      </div>
    </a-layout-content>
    
    <a-layout-footer class="footer">
      <div class="footer-content">
        <p>亿级点赞系统 ©2025 使用 Vue 3 + Ant Design Vue 构建</p>
      </div>
    </a-layout-footer>
  </a-layout>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { 
  UserOutlined, 
  LoginOutlined, 
  UserAddOutlined, 
  MenuOutlined,
  CloseOutlined,
  HomeOutlined,
  ReadOutlined,
  TeamOutlined,
  HeartFilled
} from '@ant-design/icons-vue';
import { useUserStore } from '@/store/modules/user';
import { message } from 'ant-design-vue';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const collapsed = ref(false);

// 根据路由路径设置选中的菜单项
const getSelectedKey = () => {
  const path = route.path;
  if (path === '/') return ['home'];
  if (path.startsWith('/blog')) return ['blog'];
  if (path === '/profile') return ['profile'];
  if (path === '/password') return ['password'];
  if (path === '/users') return ['users'];
  if (path === '/login') return ['login'];
  if (path === '/register') return ['register'];
  return [];
};

const selectedKeys = ref(getSelectedKey());

// 监听路由变化更新选中的菜单项
watch(() => route.path, () => {
  selectedKeys.value = getSelectedKey();
  collapsed.value = false; // 路由变化时关闭移动端菜单
});

// 退出登录处理
const logout = () => {
  userStore.logout();
  message.success('退出登录成功');
  router.push('/login');
};

// 移动端菜单处理
const handleLogout = () => {
  collapsed.value = false;
  logout();
};

const goToRoute = (path) => {
  collapsed.value = false;
  router.push(path);
};

// 监听窗口大小变化
const onResize = () => {
  if (window.innerWidth > 768 && collapsed.value) {
    collapsed.value = false;
  }
};

// 监听点击事件，点击外部关闭菜单
const onClickOutside = (e) => {
  const mobileMenu = document.querySelector('.mobile-menu-wrapper');
  const menuTrigger = document.querySelector('.mobile-trigger');
  
  if (collapsed.value && mobileMenu && !mobileMenu.contains(e.target) && !menuTrigger.contains(e.target)) {
    collapsed.value = false;
  }
};

onMounted(() => {
  window.addEventListener('resize', onResize);
  document.addEventListener('click', onClickOutside);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize);
  document.removeEventListener('click', onClickOutside);
});
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: sticky;
  top: 0;
  z-index: 1000;
  width: 100%;
  padding: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  padding: 0 24px;
  height: 64px;
}

.logo {
  display: flex;
  align-items: center;
  color: white;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  transition: color 0.3s;
}

.logo:hover {
  color: #1890ff;
}

.logo-icon {
  font-size: 24px;
  margin-right: 8px;
  color: #ff4d4f;
}

.desktop-menu {
  width: 100%;
  display: flex;
  line-height: 64px;
}

.flex-spacer {
  flex-grow: 1;
}

.mobile-trigger {
  display: none;
  font-size: 20px;
  cursor: pointer;
  padding: 0 12px;
  position: absolute;
  right: 24px;
  top: 50%;
  transform: translateY(-50%);
  color: white;
}

.mobile-menu-wrapper {
  position: fixed;
  top: 64px;
  right: -280px;
  width: 250px;
  height: calc(100vh - 64px);
  background-color: #001529;
  transition: right 0.3s ease;
  overflow-y: auto;
  z-index: 1001;
}

.mobile-menu-wrapper.active {
  right: 0;
}

.mobile-menu {
  border-right: none;
}

.mobile-menu-overlay {
  position: fixed;
  top: 64px;
  left: 0;
  width: 100%;
  height: calc(100vh - 64px);
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 1000;
}

.content-wrapper {
  flex: 1;
  background-color: #f5f7fa;
  min-height: calc(100vh - 64px - 70px);
}

.footer {
  background-color: #001529;
  color: rgba(255, 255, 255, 0.65);
  padding: 24px 0;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  text-align: center;
  padding: 0 24px;
}

@media (max-width: 768px) {
  .mobile-trigger {
    display: block;
  }
  
  .desktop-menu {
    display: none;
  }
  
  .header-content {
    padding: 0 16px;
  }
  
  .logo-text {
    font-size: 18px;
  }
}
</style> 