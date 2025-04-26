<template>
  <div class="user-list-container">
    <a-page-header
      title="用户管理"
      sub-title="管理系统用户"
    />
    
    <a-card>
      <a-form layout="inline" :model="searchForm" @finish="handleSearch">
        <a-form-item name="username" label="用户名">
          <a-input v-model:value="searchForm.username" placeholder="搜索用户名" />
        </a-form-item>
        <a-form-item name="name" label="姓名">
          <a-input v-model:value="searchForm.name" placeholder="搜索姓名" />
        </a-form-item>
        <a-form-item name="role" label="角色">
          <a-select v-model:value="searchForm.role" placeholder="选择角色" style="width: 120px">
            <a-select-option value="">全部</a-select-option>
            <a-select-option value="USER">普通用户</a-select-option>
            <a-select-option value="ADMIN">管理员</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">搜索</a-button>
          <a-button style="margin-left: 8px" @click="resetSearch">重置</a-button>
        </a-form-item>
      </a-form>
      
      <a-divider />
      
      <a-table
        :dataSource="userStore.userList"
        :columns="columns"
        rowKey="id"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'ACTIVE' ? 'green' : 'red'">
              {{ record.status === 'ACTIVE' ? '正常' : '已冻结' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'role'">
            <a-tag :color="record.role === 'ADMIN' ? 'blue' : 'default'">
              {{ record.role === 'ADMIN' ? '管理员' : '普通用户' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-popconfirm
                v-if="record.status === 'ACTIVE'"
                title="确定要冻结该用户吗？"
                @confirm="() => handleFreezeUser(record)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a-button type="primary" danger size="small">冻结</a-button>
              </a-popconfirm>
              <a-popconfirm
                v-else
                title="确定要解冻该用户吗？"
                @confirm="() => handleUnfreezeUser(record)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a-button type="primary" size="small">解冻</a-button>
              </a-popconfirm>
              <a-popconfirm
                title="确定要删除该用户吗？此操作不可撤销。"
                @confirm="() => handleDeleteUser(record)"
                ok-text="确定"
                cancel-text="取消"
              >
                <a-button type="danger" size="small">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { useUserStore } from '@/store/modules/user';

const userStore = useUserStore();
const loading = ref(false);

// 搜索表单状态
const searchForm = reactive({
  username: '',
  name: '',
  role: '',
});

// 表格列定义
const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
    width: '80px',
  },
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: '角色',
    dataIndex: 'role',
    key: 'role',
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    key: 'createdAt',
  },
  {
    title: '操作',
    key: 'action',
    fixed: 'right',
    width: '200px',
  },
];

// 分页状态
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: computed(() => userStore.total),
  showSizeChanger: true,
  pageSizeOptions: ['10', '20', '50', '100'],
});

// 加载初始数据
onMounted(() => {
  fetchUserList();
});

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true;
  try {
    await userStore.getUserList({
      page: pagination.current,
      size: pagination.pageSize,
      username: searchForm.username,
      name: searchForm.name,
      role: searchForm.role,
    });
  } catch (error) {
    message.error('加载用户列表失败');
  } finally {
    loading.value = false;
  }
};

// 处理表格变化（分页、筛选、排序）
const handleTableChange = (pag) => {
  pagination.current = pag.current;
  pagination.pageSize = pag.pageSize;
  fetchUserList();
};

// 处理搜索
const handleSearch = () => {
  pagination.current = 1; // 重置到第一页
  fetchUserList();
};

// 重置搜索表单
const resetSearch = () => {
  searchForm.username = '';
  searchForm.name = '';
  searchForm.role = '';
  pagination.current = 1;
  fetchUserList();
};

// 处理冻结用户
const handleFreezeUser = async (user) => {
  try {
    await userStore.freezeUser(user.id);
    message.success(`用户 ${user.username} 已被冻结`);
    fetchUserList(); // 刷新列表
  } catch (error) {
    message.error(error.message || '冻结用户失败');
  }
};

// 处理解冻用户
const handleUnfreezeUser = async (user) => {
  try {
    await userStore.unfreezeUser(user.id);
    message.success(`用户 ${user.username} 已被解冻`);
    fetchUserList(); // 刷新列表
  } catch (error) {
    message.error(error.message || '解冻用户失败');
  }
};

// 处理删除用户
const handleDeleteUser = async (user) => {
  try {
    await userStore.deleteUser(user.id);
    message.success(`用户 ${user.username} 已被删除`);
    fetchUserList(); // 刷新列表
  } catch (error) {
    message.error(error.message || '删除用户失败');
  }
};
</script>

<style scoped>
.user-list-container {
  max-width: 1200px;
  margin: 0 auto;
}
</style> 