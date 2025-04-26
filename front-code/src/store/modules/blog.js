import { defineStore } from 'pinia';
import blogApi from '@/api/blog';
import thumbApi from '@/api/thumb';

export const useBlogStore = defineStore('blog', {
  state: () => ({
    blogs: [],
    currentBlog: null,
    total: 0,
    loading: false,
    likeStatus: {},
    likeCounts: {}
  }),

  getters: {
    getBlogById: (state) => (id) => {
      return state.blogs.find(blog => blog.id === id) || null;
    }
  },

  actions: {
    // Get blog list
    async getBlogList(params) {
      try {
        this.loading = true;
        const response = await blogApi.getBlogList(params);
        
        // 适配API返回格式
        if (response && response.code === 0 && response.data) {
          const { list, total } = response.data;
          this.blogs = list || [];
          this.total = total || 0;
          
          // Fetch like counts for each blog
          list.forEach(async blog => {
            this.getLikeCount(blog.id);
            this.checkLikeStatus(blog.id);
          });
          
          return { list, total };
        } else {
          throw new Error('获取博客列表失败');
        }
      } catch (error) {
        console.error('获取博客列表错误:', error);
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // Get blog detail
    async getBlogDetail(blogId) {
      try {
        this.loading = true;
        const response = await blogApi.getBlogDetail(blogId);
        
        // 适配API返回格式
        if (response && response.code === 0 && response.data) {
          this.currentBlog = response.data;
          
          // Get like status and count
          await this.checkLikeStatus(blogId);
          await this.getLikeCount(blogId);
          
          return response.data;
        } else {
          throw new Error('获取博客详情失败');
        }
      } catch (error) {
        console.error('获取博客详情错误:', error);
        return Promise.reject(error);
      } finally {
        this.loading = false;
      }
    },

    // Like a blog
    async likeBlog(blogId) {
      try {
        const response = await thumbApi.likeBlog(blogId);
        
        // 更新当前博客的点赞状态
        if (this.currentBlog && this.currentBlog.id === blogId) {
          this.currentBlog.hasThumb = true;
          this.currentBlog.thumbCount = (this.currentBlog.thumbCount || 0) + 1;
        }
        
        // 更新列表中博客的点赞状态
        const blog = this.blogs.find(b => b.id === blogId);
        if (blog) {
          blog.hasThumb = true;
          blog.thumbCount = (blog.thumbCount || 0) + 1;
        }
        
        return response;
      } catch (error) {
        console.error('点赞失败:', error);
        return Promise.reject(error);
      }
    },

    // Unlike a blog
    async unlikeBlog(blogId) {
      try {
        const response = await thumbApi.unlikeBlog(blogId);
        
        // 更新当前博客的点赞状态
        if (this.currentBlog && this.currentBlog.id === blogId) {
          this.currentBlog.hasThumb = false;
          this.currentBlog.thumbCount = Math.max(0, (this.currentBlog.thumbCount || 1) - 1);
        }
        
        // 更新列表中博客的点赞状态
        const blog = this.blogs.find(b => b.id === blogId);
        if (blog) {
          blog.hasThumb = false;
          blog.thumbCount = Math.max(0, (blog.thumbCount || 1) - 1);
        }
        
        return response;
      } catch (error) {
        console.error('取消点赞失败:', error);
        return Promise.reject(error);
      }
    },

    // Check if user has liked a blog
    async checkLikeStatus(blogId) {
      try {
        const { liked } = await thumbApi.checkLikeStatus(blogId);
        this.likeStatus[blogId] = liked;
        return liked;
      } catch (error) {
        console.error('Failed to check like status:', error);
        this.likeStatus[blogId] = false;
        return false;
      }
    },

    // Get like count for a blog
    async getLikeCount(blogId) {
      try {
        const { count } = await thumbApi.getLikeCount(blogId);
        this.likeCounts[blogId] = count;
        return count;
      } catch (error) {
        console.error('Failed to get like count:', error);
        this.likeCounts[blogId] = 0;
        return 0;
      }
    }
  }
}); 