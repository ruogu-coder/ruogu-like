<template>
  <div class="blog-detail-container">
    <a-page-header 
      :title="blog?.title || '博客详情'" 
      :sub-title="formatCategory(blog?.category)"
      @back="() => router.push('/blog')"
    />
    
    <a-skeleton active :loading="loading" v-if="loading">
    </a-skeleton>
    
    <template v-else-if="blog">
      <a-card>
        <template #title>
          <div class="blog-title">
            <h1>{{ blog.title }}</h1>
            <div class="blog-meta">
              <a-tag>{{ blog.category }}</a-tag>
              <span class="blog-date">{{ formatDate(blog.createdAt) }}</span>
              <span class="blog-author">作者: {{ blog.author?.name || '未知' }}</span>
              <span class="blog-views"><EyeOutlined /> {{ blog.viewCount || 0 }} 阅读量</span>
              <span class="blog-likes">
                <ThumbButton :blogId="blog.id" />
              </span>
            </div>
          </div>
        </template>
        
        <div class="blog-content">
          <div v-html="formattedContent"></div>
        </div>
        
        <a-divider />
        
        <div class="blog-actions">
          <a-button type="primary" @click="() => router.push('/blog')">
            返回博客列表
          </a-button>
        </div>
      </a-card>
    </template>
    
    <a-empty v-else description="找不到博客" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { EyeOutlined } from '@ant-design/icons-vue';
import { useBlogStore } from '@/store/modules/blog';
import ThumbButton from '@/components/ThumbButton.vue';

const route = useRoute();
const router = useRouter();
const blogStore = useBlogStore();
const loading = ref(true);

const blogId = computed(() => route.params.id);
const blog = computed(() => blogStore.currentBlog);

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知日期';
  return new Date(dateString).toLocaleDateString();
};

// 格式化分类用于副标题
const formatCategory = (category) => {
  return category ? `分类: ${category}` : '';
};

// 格式化博客内容，处理类似Markdown的语法
const formattedContent = computed(() => {
  if (!blog.value?.content) return '';
  
  // 将换行符替换为<br>标签
  let content = blog.value.content.replace(/\n/g, '<br>');
  
  // 转换类似Markdown的标题样式 (# 标题)
  content = content.replace(/# (.*?)(<br|$)/g, '<h2>$1</h2>$2');
  content = content.replace(/## (.*?)(<br|$)/g, '<h3>$1</h3>$2');
  content = content.replace(/### (.*?)(<br|$)/g, '<h4>$1</h4>$2');
  
  // 转换类似Markdown的粗体样式 (**文本**)
  content = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
  
  // 转换类似Markdown的斜体样式 (*文本*)
  content = content.replace(/\*(.*?)\*/g, '<em>$1</em>');
  
  return content;
});

// 加载博客数据
onMounted(async () => {
  loading.value = true;
  try {
    await blogStore.getBlogDetail(blogId.value);
  } catch (error) {
    message.error('加载博客详情失败');
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.blog-detail-container {
  max-width: 900px;
  margin: 0 auto;
}

.blog-title h1 {
  margin-bottom: 8px;
}

.blog-meta {
  color: rgba(0, 0, 0, 0.45);
  margin-bottom: 24px;
}

.blog-date,
.blog-author,
.blog-views {
  margin-left: 16px;
}

.blog-content {
  font-size: 16px;
  line-height: 1.8;
  margin: 24px 0;
}

.blog-actions {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style> 