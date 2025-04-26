<template>
  <div class="login-container">
    <a-card title="登录" class="login-card">
      <a-form
        :model="formState"
        name="login"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          label="用户名"
          name="username"
          :rules="[{ required: true, message: '请输入用户名!' }]"
        >
          <a-input v-model:value="formState.username" />
        </a-form-item>

        <a-form-item
          label="密码"
          name="password"
          :rules="[{ required: true, message: '请输入密码!' }]"
        >
          <a-input-password v-model:value="formState.password" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
          <a-button type="primary" html-type="submit" :loading="loading">
            登录
          </a-button>
          <a-button type="link">
            <router-link to="/register">立即注册!</router-link>
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import { useUserStore } from '@/store/modules/user';

const userStore = useUserStore();
const router = useRouter();
const route = useRoute();

const loading = ref(false);
const formState = reactive({
  username: '',
  password: '',
});

const onFinish = async (values) => {
  try {
    loading.value = true;
    await userStore.login({
      userAccount: values.username,
      userPassword: values.password,
    });
    message.success('登录成功！');
    
    // 使用Vue Router导航
    await router.push('/');
  } catch (error) {
    console.error('登录错误:', error);
    message.error(error?.message || '登录失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('表单验证失败:', errorInfo);
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}
.login-card {
  width: 500px;
  max-width: 90%;
}
</style> 