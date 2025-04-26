import axios from 'axios';

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  withCredentials: true,  // 启用凭证，确保跨域请求发送cookie
});

// Request interceptor
request.interceptors.request.use(
  config => {
    const token = sessionStorage.getItem('ruogu-like-token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// Response interceptor
request.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    const { status, data } = error.response || {};
    if (status === 401) {
      // Token过期或未授权
      sessionStorage.removeItem('ruogu-like-token');
      localStorage.removeItem('userInfo');
      
      // 直接使用location.href强制跳转到登录页面
      if (window.location.pathname !== '/login') {
        setTimeout(() => {
          window.location.href = '/login';
        }, 100);
      }
    }
    return Promise.reject(data || error);
  }
);

export default request; 