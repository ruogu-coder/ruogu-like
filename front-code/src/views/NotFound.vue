<template>
  <div class="not-found">
    <a-result
      status="404"
      title="404"
      sub-title="抱歉，您访问的页面不存在。"
    >
      <template #extra>
        <a-button type="primary">
          <router-link to="/">返回首页</router-link>
        </a-button>
      </template>
    </a-result>
  </div>
</template>

<script setup>
// 此组件不需要逻辑
</script>

<style scoped>
.not-found {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}
</style> 