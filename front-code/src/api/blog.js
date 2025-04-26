import request from './request';

// Blog API endpoints
export default {
  // Get blog list
  getBlogList(params) {
    return request({
      url: '/blog/page',
      method: 'get',
      params
    });
  },

  // Get blog details
  getBlogDetail(blogId) {
    return request({
      url: `/blog/getBlogVoById`,
      method: 'get',
      params: { id: blogId }
    });
  },
  
  // 点赞博客
  likeBlog(blogId) {
    return request({
      url: `/thumb/do`,
      method: 'post',
      data: { blogId }
    });
  },
  
  // 取消点赞
  unlikeBlog(blogId) {
    return request({
      url: `/thumb/undo`,
      method: 'post',
      data: { blogId }
    });
  }
}; 