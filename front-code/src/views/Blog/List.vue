<template>
  <div class="blog-list-container">
    <a-page-header
      title="博客列表"
      sub-title="浏览所有博客文章"
    />
    
    <a-card>
      <a-form layout="inline" :model="searchForm" @finish="handleSearch">
        <a-form-item name="title" label="标题">
          <a-input v-model:value="searchForm.title" placeholder="搜索博客标题" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">搜索</a-button>
          <a-button style="margin-left: 8px" @click="resetSearch">重置</a-button>
        </a-form-item>
      </a-form>
      
      <a-divider />
      
      <a-list
        :loading="loading"
        itemLayout="vertical"
        size="large"
        :dataSource="blogStore.blogs"
        :pagination="pagination"
      >
        <template #renderItem="{ item }">
          <a-list-item key="item.id">
            <template #extra>
              <div class="blog-stats">
                <ThumbButton :blogId="item.id" />
              </div>
            </template>
            
            <a-list-item-meta>
              <template #title>
                <router-link :to="`/blog/${item.id}`">{{ item.title }}</router-link>
              </template>
              <template #description>
                <div>
                  <a-tag>{{ item.category }}</a-tag>
                  <span class="blog-date">{{ formatDate(item.createdAt) }}</span>
                  <span class="blog-author">作者: {{ item.author?.name || '未知' }}</span>
                </div>
              </template>
            </a-list-item-meta>
            
            <div class="blog-summary">{{ truncateContent(item.content) }}</div>
            
            <template #actions>
              <span>
                <EyeOutlined /> {{ item.viewCount || 0 }} 阅读量
              </span>
              <span>
                <router-link :to="`/blog/${item.id}`">阅读更多</router-link>
              </span>
            </template>
          </a-list-item>
        </template>
      </a-list>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { EyeOutlined } from '@ant-design/icons-vue';
import { useBlogStore } from '@/store/modules/blog';
import ThumbButton from '@/components/ThumbButton.vue';

const blogStore = useBlogStore();
const loading = ref(false);

// 搜索表单状态
const searchForm = reactive({
  title: '',
});

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知日期';
  return new Date(dateString).toLocaleDateString();
};

// 截断内容
const truncateContent = (content, length = 200) => {
  if (!content) return '';
  if (content.length <= length) return content;
  return content.substring(0, length) + '...';
};

// 分页配置
const pagination = reactive({
  onChange: (page, pageSize) => {
    pagination.current = page;
    pagination.pageSize = pageSize;
    fetchBlogs();
  },
  current: 1,
  pageSize: 10,
  total: computed(() => blogStore.total),
  showSizeChanger: true,
  pageSizeOptions: ['10', '20', '50'],
});

// 加载初始数据
onMounted(() => {
  fetchBlogs();
});

// 获取博客列表
const fetchBlogs = async () => {
  loading.value = true;
  try {
    await blogStore.getBlogList({
      page: pagination.current,
      size: pagination.pageSize,
      title: searchForm.title,
    });
  } catch (error) {
    message.error('加载博客列表失败');
  } finally {
    loading.value = false;
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1; // 重置到第一页
  fetchBlogs();
};

// 重置搜索表单
const resetSearch = () => {
  searchForm.title = '';
  pagination.current = 1;
  fetchBlogs();
};
</script>

<style scoped>
.blog-list-container {
  max-width: 1000px;
  margin: 0 auto;
}
.blog-summary {
  margin: 16px 0;
  color: rgba(0, 0, 0, 0.65);
}
.blog-date {
  margin-left: 8px;
  color: rgba(0, 0, 0, 0.45);
}
.blog-author {
  margin-left: 16px;
  color: rgba(0, 0, 0, 0.65);
}
.blog-stats {
  text-align: center;
}
</style> 