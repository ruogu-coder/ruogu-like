<template>
  <div class="home-page">
    <!-- 简单导航栏 -->
    <div class="nav-bar">
      <div class="nav-content">
        <div class="logo">
          <HeartFilled class="logo-icon" />
          <span class="site-name">亿级点赞</span>
        </div>
        
        <div class="nav-links">
          <a-button type="primary" @click="$router.push('/main/blog')">进入博客</a-button>
          <a-button v-if="isLoggedIn" @click="$router.push('/main/profile')">个人中心</a-button>
          <a-button v-if="!isLoggedIn" @click="$router.push('/login')">登录</a-button>
          <a-button v-if="!isLoggedIn" @click="$router.push('/register')">注册</a-button>
        </div>
      </div>
    </div>
    
    <div class="content-container">
      <a-spin :spinning="loading" :tip="'载入中...'" class="spin-container">
        <template v-if="blogs.length > 0">
          <!-- 博客列表 -->
          <div class="article-grid">
            <div 
              v-for="blog in blogs" 
              :key="blog.id" 
              class="article-card"
              @click="navigateToBlog(blog.id)"
            >
              <div class="card-cover" v-if="blog.coverImg">
                <img 
                  :src="blog.coverImg" 
                  :alt="blog.title" 
                  class="cover-image"
                  loading="lazy"
                />
              </div>
              <div v-else class="card-cover cover-placeholder">
                <FileTextOutlined />
              </div>
              
              <div class="article-content">
                <h3 class="article-title">{{ blog.title }}</h3>
                <p class="article-desc">{{ truncateContent(blog.content) }}</p>
                
                <div class="article-meta">
                  <div class="meta-date">
                    <CalendarOutlined />
                    <span>{{ formatDate(blog.createTime) }}</span>
                  </div>
                  
                  <div 
                    class="meta-likes" 
                    :class="{ 'is-liked': blog.hasThumb }"
                    @click.stop="toggleLike(blog)"
                  >
                    <LikeOutlined />
                    <span>{{ blog.thumbCount || 0 }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 分页控件 -->
          <div class="pagination-area">
            <a-pagination
              v-model:current="pagination.current"
              :pageSize="pagination.pageSize"
              :total="total"
              @change="handlePageChange"
              show-quick-jumper
              :hideOnSinglePage="true"
            />
          </div>
        </template>
        
        <!-- 空状态 -->
        <template v-else-if="!loading">
          <a-empty
            :image="Empty.PRESENTED_IMAGE_SIMPLE"
            description="暂无内容"
            class="empty-container"
          >
            <a-button type="primary" @click="fetchBlogs">刷新</a-button>
          </a-empty>
        </template>
      </a-spin>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { 
  FileTextOutlined, 
  CalendarOutlined, 
  LikeOutlined, 
  RightOutlined,
  HeartFilled
} from '@ant-design/icons-vue';
import { Empty, message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/modules/user';
import blogApi from '@/api/blog';

const router = useRouter();
const userStore = useUserStore();



// 博客数据
const blogs = ref([]);
const loading = ref(false);
const total = ref(0);

// 分页
const pagination = ref({
  current: 1,
  pageSize: 12,
});

// 获取博客列表
const fetchBlogs = async () => {
  loading.value = true;
  try {
    const response = await blogApi.getBlogList({
      pageNo: pagination.value.current,
      pageSize: pagination.value.pageSize
    });
    
    if (response && response.code === 0 && response.data) {
      blogs.value = response.data.list || [];
      total.value = parseInt(response.data.total) || 0;
    } else {
      console.error('Failed to fetch blogs:', response ? response.msg : 'Unknown error');
    }
  } catch (error) {
    console.error('Error fetching blogs:', error);
  } finally {
    loading.value = false;
  }
};

// 分页变化处理
const handlePageChange = (page) => {
  pagination.value.current = page;
  fetchBlogs();
};

// 跳转到博客详情
const navigateToBlog = (blogId) => {
  router.push({
    path: `/main/blog/detail/${blogId}`
  });
};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知日期';
  const date = new Date(dateString);
  return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日';
};

// 截断内容
const truncateContent = (content, length = 80) => {
  if (!content) return '';
  if (content.length <= length) return content;
  return content.substring(0, length) + '...';
};

// 切换点赞状态
const toggleLike = async (blog) => {

  
  try {
    if (blog.hasThumb) {
      // 取消点赞
      const response = await blogApi.unlikeBlog(blog.id);
      if (response && response.code === 0) {
        blog.hasThumb = false;
        blog.thumbCount = Math.max(0, (blog.thumbCount || 1) - 1);
        message.success('已取消点赞');
      }
    } else {
      // 点赞
      const response = await blogApi.likeBlog(blog.id);
      if (response && response.code === 0) {
        blog.hasThumb = true;
        blog.thumbCount = (blog.thumbCount || 0) + 1;
        message.success('点赞成功');
      }
    }
  } catch (error) {
    console.error('点赞操作失败:', error);
    message.error('操作失败，请稍后重试');
  }
};

// 组件挂载时获取数据
onMounted(() => {
  fetchBlogs();
});
</script>

<style scoped lang="less">
.home-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.nav-bar {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  padding: 10px 0;
  
  .nav-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .logo {
    display: flex;
    align-items: center;
    cursor: pointer;
    
    .logo-icon {
      font-size: 22px;
      color: #ff4d4f;
      margin-right: 8px;
    }
    
    .site-name {
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }
  }
  
  .nav-links {
    display: flex;
    gap: 10px;
  }
}

.content-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 16px;
}

.spin-container {
  min-height: 400px;
  display: flex;
  flex-direction: column;
}

.empty-container {
  margin-top: 80px;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.article-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  display: flex;
  flex-direction: column;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  }
  
  .card-cover {
    height: 180px;
    overflow: hidden;
    position: relative;
    background-color: #f5f5f5;
    
    .cover-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
    
    &.cover-placeholder {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 48px;
      color: #d9d9d9;
    }
  }
  
  &:hover .cover-image {
    transform: scale(1.05);
  }
  
  .article-content {
    padding: 16px;
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .article-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 8px;
    color: #333;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .article-desc {
    font-size: 14px;
    color: #666;
    margin-bottom: 12px;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .article-meta {
    display: flex;
    justify-content: space-between;
    color: #999;
    font-size: 13px;
  }
  
  .article-author {
    display: flex;
    align-items: center;
    gap: 6px;
  }
  
  .meta-item {
    display: flex;
    align-items: center;
    gap: 4px;
  }
  
  .meta-likes {
    display: flex;
    align-items: center;
    gap: 4px;
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.2s;
    
    &:hover {
      background-color: #f0f0f0;
      color: #1890ff;
    }
    
    &.is-liked {
      color: #1890ff;
    }
  }
}

.pagination-area {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

// 响应式调整
@media screen and (max-width: 768px) {
  .article-grid {
    grid-template-columns: repeat(auto-fill, minmax(100%, 1fr));
  }
  
  .nav-content {
    flex-direction: column;
    gap: 10px;
  }
  
  .nav-links {
    width: 100%;
    justify-content: center;
  }
}
</style> 