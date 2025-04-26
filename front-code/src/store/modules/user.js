import { defineStore } from 'pinia';
import userApi from '@/api/user';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: sessionStorage.getItem('ruogu-like-token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    userList: [],
    total: 0,
    loading: false
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.userInfo?.role === 'ADMIN'
  },

  actions: {
    // 登录动作
    async login(loginData) {
      try {
        this.loading = true;
        const response = await userApi.login(loginData);
        console.log(response);
        
        // 确保响应中包含data
        if (!response) {
          throw new Error('登录响应数据格式错误');
        }
        
        const { data } = response;
        
        // 存储用户信息到localStorage
        localStorage.setItem('userInfo', JSON.stringify(data || {}));
        
        // token已经由后端设置为cookie，这里只更新状态
        this.token = sessionStorage.getItem('ruogu-like-token') || '';
        this.userInfo = data || {};
        
        return Promise.resolve(response);
      } catch (error) {
        console.error('登录失败:', error);
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // 注册动作
    async register(userData) {
      try {
        this.loading = true;
        return await userApi.register(userData);
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // 登出动作
    logout() {
      // 清除存储
      sessionStorage.removeItem('ruogu-like-token');
      localStorage.removeItem('userInfo');
      
      // 重置状态
      this.token = '';
      this.userInfo = {};
    },

    // 获取用户资料
    async getProfile() {
      try {
        this.loading = true;
        const userInfo = await userApi.getProfile();
        this.userInfo = userInfo;
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        return userInfo;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // 更新用户资料
    async updateProfile(profileData) {
      try {
        this.loading = true;
        const result = await userApi.updateProfile(profileData);
        // 更新本地用户信息
        await this.getProfile();
        return result;
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // 修改密码
    async changePassword(passwordData) {
      try {
        this.loading = true;
        return await userApi.changePassword(passwordData);
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // 获取用户列表（管理员）
    async getUserList(params) {
      try {
        this.loading = true;
        const { records, total } = await userApi.getUserList(params);
        this.userList = records;
        this.total = total;
        return { records, total };
      } catch (error) {
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // 冻结用户（管理员）
    async freezeUser(userId) {
      try {
        return await userApi.freezeUser(userId);
      } catch (error) {
        return Promise.reject(error);
      }
    },

    // 解冻用户（管理员）
    async unfreezeUser(userId) {
      try {
        return await userApi.unfreezeUser(userId);
      } catch (error) {
        return Promise.reject(error);
      }
    },

    // 删除用户（管理员）
    async deleteUser(userId) {
      try {
        return await userApi.deleteUser(userId);
      } catch (error) {
        return Promise.reject(error);
      }
    }
  }
}); 