<template>
  <div class="thumb-button">
    <a-button
      type="text"
      :loading="loading"
      @click.stop="handleThumb"
      class="thumb-btn"
      :class="{ 'has-thumbed': hasThumb }"
    >
      <template #icon>
        <like-filled v-if="hasThumb" />
        <like-outlined v-else />
      </template>
      <span>{{ thumbCount }}</span>
    </a-button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { message } from 'ant-design-vue';
import { LikeFilled, LikeOutlined } from '@ant-design/icons-vue';
import { useUserStore } from '@/store/modules/user';
import blogApi from '@/api/blog';

const props = defineProps({
  blog: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['thumb-updated']);
const userStore = useUserStore();
const loading = ref(false);

// 计算属性，用于检查当前博客是否已点赞
const hasThumb = computed(() => props.blog.hasThumb || false);

// 计算属性，用于获取点赞数
const thumbCount = computed(() => props.blog.thumbCount || 0);

// 处理点赞/取消点赞
const handleThumb = async (e) => {
  // 阻止点击事件冒泡，避免触发卡片的点击
  e.stopPropagation();
  
  
  try {
    loading.value = true;
    
    if (hasThumb.value) {
      // 取消点赞
      const response = await blogApi.unlikeBlog(props.blog.id);
      if (response && response.code === 0) {
        message.success('已取消点赞');
        // 通知父组件更新点赞状态
        emit('thumb-updated', props.blog.id, false, (thumbCount.value - 1));
      } else {
        throw new Error(response?.msg || '取消点赞失败');
      }
    } else {
      // 点赞
      const response = await blogApi.likeBlog(props.blog.id);
      if (response && response.code === 0) {
        message.success('点赞成功');
        // 通知父组件更新点赞状态
        emit('thumb-updated', props.blog.id, true, (thumbCount.value + 1));
      } else {
        throw new Error(response?.msg || '点赞失败');
      }
    }
  } catch (error) {
    message.error(error?.message || '操作失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.thumb-button {
  display: inline-block;
}

.thumb-btn {
  display: flex;
  align-items: center;
  padding: 0 8px;
  height: 32px;
  border-radius: 16px;
  transition: all 0.3s;
}

.thumb-btn.has-thumbed {
  color: #1890ff;
}

.thumb-btn:hover {
  background-color: rgba(24, 144, 255, 0.1);
}
</style> 