package com.ruogu.thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.model.dto.blog.BlogPageReqDTO;
import com.ruogu.thumb.model.entity.Blog;
import com.ruogu.thumb.model.vo.blog.BlogVO;

/**
* @author ruogu
* @description 针对表【blog(博客表)】的数据库操作Service
* @createDate 2025-04-18 13:32:05
*/
public interface BlogService extends IService<Blog> {

    /**
     * 根据id获取博客信息
     * @param id 博客ID
     * @return 博客信息
     */
    BlogVO getBlogById(Long id);

    /**
     * 分页获取博客列表
     * @param blogPageReqDTO 请求参数
     * @return 博客分页信息
     */
    PageResult<BlogVO> getBlogPage(BlogPageReqDTO blogPageReqDTO);
}
