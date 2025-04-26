<template>
  <div class="profile-container">
    <a-page-header
      title="个人资料"
      sub-title="查看和编辑您的个人信息"
    />
    
    <a-card class="profile-card" :loading="loading">
      <a-form
        :model="formState"
        name="profile"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
        autocomplete="off"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item
          label="用户名"
          name="username"
        >
          <a-input v-model:value="formState.username" disabled />
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

        <a-form-item
          label="头像URL"
          name="avatar"
        >
          <a-input v-model:value="formState.avatar" />
          <div class="avatar-preview" v-if="formState.avatar">
            <img :src="formState.avatar" alt="头像" />
          </div>
        </a-form-item>

        <a-form-item
          label="个人简介"
          name="bio"
        >
          <a-textarea v-model:value="formState.bio" :rows="4" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 6, span: 18 }">
          <a-button type="primary" html-type="submit" :loading="submitting">
            保存修改
          </a-button>
          <a-button style="margin-left: 10px">
            <router-link to="/password">修改密码</router-link>
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { useUserStore } from '@/store/modules/user';

const userStore = useUserStore();
const loading = ref(true);
const submitting = ref(false);

const formState = reactive({
  username: '',
  name: '',
  email: '',
  avatar: '',
  bio: '',
});

// 获取用户资料数据
onMounted(async () => {
  try {
    const userInfo = await userStore.getProfile();
    formState.username = userInfo.username;
    formState.name = userInfo.name || '';
    formState.email = userInfo.email || '';
    formState.avatar = userInfo.avatar || '';
    formState.bio = userInfo.bio || '';
  } catch (error) {
    message.error('加载资料数据失败');
  } finally {
    loading.value = false;
  }
});

const onFinish = async (values) => {
  submitting.value = true;
  try {
    await userStore.updateProfile({
      name: values.name,
      email: values.email,
      avatar: values.avatar,
      bio: values.bio,
    });
    message.success('个人资料更新成功');
  } catch (error) {
    message.error(error.message || '更新资料失败');
  } finally {
    submitting.value = false;
  }
};

const onFinishFailed = (errorInfo) => {
  console.log('表单验证失败:', errorInfo);
};
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
}
.profile-card {
  margin-top: 20px;
}
.avatar-preview {
  margin-top: 10px;
}
.avatar-preview img {
  max-width: 100px;
  max-height: 100px;
  border-radius: 50%;
}
</style> 