import request from './request';

// User API endpoints
export default {
  // User registration
  register(userData) {
    return request({
      url: '/user/register',
      method: 'post',
      data: userData
    });
  },

  // User login
  login(loginData) {
    return request({
      url: '/user/login',
      method: 'post',
      data: loginData
    });
  },

  // Get user profile
  getProfile() {
    return request({
      url: '/user/profile',
      method: 'get'
    });
  },

  // Update user profile
  updateProfile(profileData) {
    return request({
      url: '/user/profile',
      method: 'put',
      data: profileData
    });
  },

  // Change password
  changePassword(passwordData) {
    return request({
      url: '/user/password',
      method: 'put',
      data: passwordData
    });
  },

  // Get user list (admin)
  getUserList(params) {
    return request({
      url: '/user/list',
      method: 'get',
      params
    });
  },

  // Freeze user (admin)
  freezeUser(userId) {
    return request({
      url: `/user/freeze/${userId}`,
      method: 'put'
    });
  },

  // Unfreeze user (admin)
  unfreezeUser(userId) {
    return request({
      url: `/user/unfreeze/${userId}`,
      method: 'put'
    });
  },

  // Delete user (admin)
  deleteUser(userId) {
    return request({
      url: `/user/${userId}`,
      method: 'delete'
    });
  }
}; 