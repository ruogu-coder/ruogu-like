import request from './request';

// Thumb (Like) API endpoints
export default {
  // Like a blog
  likeBlog(blogId) {
    return request({
      url: `/thumb/like/${blogId}`,
      method: 'post'
    });
  },

  // Unlike a blog
  unlikeBlog(blogId) {
    return request({
      url: `/thumb/unlike/${blogId}`,
      method: 'post'
    });
  },

  // Check if user liked a blog
  checkLikeStatus(blogId) {
    return request({
      url: `/thumb/status/${blogId}`,
      method: 'get'
    });
  },

  // Get like count for a blog
  getLikeCount(blogId) {
    return request({
      url: `/thumb/count/${blogId}`,
      method: 'get'
    });
  }
}; 