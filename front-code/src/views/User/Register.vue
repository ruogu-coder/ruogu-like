<template>
  <div class="register-container">
    <a-card title="注册" class="register-card">
      <a-form
        :model="formState"
        name="register"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          label="用户名"
          name="username"
          :rules="[
            { required: true, message: '请输入用户名!' },
            { min: 4, max: 20, message: '用户名长度必须在4-20个字符之间' }
          ]"
        >
          <a-input v-model:value="formState.username" />
        </a-form-item>

        <a-form-item
          label="密码"
          name="password"
          :rules="[
            { required: true, message: '请输入密码!' },
            { min: 6, message: '密码长度不能少于6个字符' }
          ]"
        >
          <a-input-password v-model:value="formState.password" />
        </a-form-item>

        <a-form-item
          label="确认密码"
          name="confirmPassword"
          :rules="[
            { required: true, message: '请确认密码!' },
            { validator: validateConfirmPassword }
          ]"
        >
          <a-input-password v-model:value="formState.confirmPassword" />
        </a-form-item>

        <a-form-item
          label="姓名"
          name="name"
          :rules="[{ required: true, message: '请输入姓名!' }]"
        >
          <a-input v-model:value="formState.name" />
        </a-form-item>

        <a-form-item
          label="邮箱"
          name="email"
          :rules="[
            { type: 'email', message: '请输入有效的邮箱地址!' },
            { required: true, message: '请输入邮箱!' }
          ]"
        >
          <a-input v-model:value="formState.email" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
          <a-button type="primary" html-type="submit" :loading="loading">
            注册
          </a-button>
          <a-button type="link">
            <router-link to="/login">已有账号？立即登录</router-link>
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useUserStore } from '@/store/modules/user';

const userStore = useUserStore();
const router = useRouter();

const loading = ref(false);
const formState = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  email: '',
});

// 验证确认密码
const validateConfirmPassword = async (rule, value) => {
  if (value === '') {
    return Promise.reject('请确认您的密码!');
  }
  if (value !== formState.password) {
    return Promise.reject('两次输入的密码不一致!');
  }
  return Promise.resolve();
};

const onFinish = async (values) => {
  loading.value = true;
  try {
    await userStore.register({
      username: values.username,
      password: values.password,
      name: values.name,
      email: values.email,
    });
    
    message.success('注册成功！请登录。');
    router.push('/login');
  } catch (error) {
    message.error(error.message || '注册失败');
  } finally {
    loading.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('表单验证失败:', errorInfo);
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 20px 0;
}
.register-card {
  width: 500px;
  max-width: 90%;
}
</style> 