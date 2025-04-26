<template>
  <div class="password-container">
    <a-page-header
      title="修改密码"
      sub-title="更新您的账户密码"
    />
    
    <a-card class="password-card">
      <a-form
        :model="formState"
        name="passwordForm"
        :label-col="{ span: 8 }"
        :wrapper-col="{ span: 16 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          label="当前密码"
          name="oldPassword"
          :rules="[{ required: true, message: '请输入当前密码!' }]"
        >
          <a-input-password v-model:value="formState.oldPassword" />
        </a-form-item>

        <a-form-item
          label="新密码"
          name="newPassword"
          :rules="[
            { required: true, message: '请输入新密码!' },
            { min: 6, message: '密码长度不能少于6个字符' }
          ]"
        >
          <a-input-password v-model:value="formState.newPassword" />
        </a-form-item>

        <a-form-item
          label="确认新密码"
          name="confirmPassword"
          :rules="[
            { required: true, message: '请确认新密码!' },
            { validator: validateConfirmPassword }
          ]"
        >
          <a-input-password v-model:value="formState.confirmPassword" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
          <a-button type="primary" html-type="submit" :loading="loading">
            修改密码
          </a-button>
          <a-button style="margin-left: 10px">
            <router-link to="/profile">返回个人资料</router-link>
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
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
});

// 验证确认密码
const validateConfirmPassword = async (rule, value) => {
  if (value === '') {
    return Promise.reject('请确认您的新密码!');
  }
  if (value !== formState.newPassword) {
    return Promise.reject('两次输入的新密码不一致!');
  }
  return Promise.resolve();
};

const onFinish = async (values) => {
  loading.value = true;
  try {
    await userStore.changePassword({
      oldPassword: values.oldPassword,
      newPassword: values.newPassword,
    });
    
    message.success('密码修改成功');
    // 重置表单
    formState.oldPassword = '';
    formState.newPassword = '';
    formState.confirmPassword = '';
    
    // 重定向到个人资料页面
    router.push('/profile');
  } catch (error) {
    message.error(error.message || '密码修改失败');
  } finally {
    loading.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('表单验证失败:', errorInfo);
};
</script>

<style scoped>
.password-container {
  max-width: 600px;
  margin: 0 auto;
}
.password-card {
  margin-top: 20px;
}
</style> 