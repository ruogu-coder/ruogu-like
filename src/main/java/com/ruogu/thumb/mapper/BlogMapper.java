package com.ruogu.thumb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.core.mapper.BaseMapperPlus;
import com.ruogu.thumb.model.dto.blog.BlogPageReqDTO;
import com.ruogu.thumb.model.entity.Blog;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Objects;

/**
 * @author ruogu
 * @description 针对表【blog(博客表)】的数据库操作Mapper
 * @createDate 2025-04-18 13:32:05
 * @Entity generator.domain.Blog
 */
public interface BlogMapper extends BaseMapperPlus<Blog> {

    /**
     * 批量更新博客点赞数量
     * @param countMap 博客ID -> 点赞数
     */
    void batchUpdateThumbCount(@Param("countMap") Map<Long, Long> countMap);



    /**
     * 分页查询博客信息
     *
     * @param blogPageReqDTO 请求参数
     * @return 博客信息
     */
    default PageResult<Blog> getBlogPage(BlogPageReqDTO blogPageReqDTO) {
        return selectPage(blogPageReqDTO, new LambdaQueryWrapper<Blog>()
                .like(Objects.nonNull(blogPageReqDTO.getBlogName()), Blog::getTitle, blogPageReqDTO.getBlogName()));
    }
}




