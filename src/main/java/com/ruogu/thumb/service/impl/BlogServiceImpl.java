package com.ruogu.thumb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.mapper.BlogMapper;
import com.ruogu.thumb.model.dto.blog.BlogPageReqDTO;
import com.ruogu.thumb.model.entity.Blog;
import com.ruogu.thumb.model.entity.Thumb;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.model.vo.blog.BlogVO;
import com.ruogu.thumb.service.BlogService;
import com.ruogu.thumb.service.ThumbService;
import com.ruogu.thumb.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ruogu
 * @description 针对表【blog(博客表)】的数据库操作Service实现
 * @createDate 2025-04-18 13:32:05
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
        implements BlogService {

    @Resource
    private UserService userService;


    @Resource
    @Lazy
    private ThumbService thumbService;

    @Resource
    private BlogMapper blogMapper;


    @Override
    public BlogVO getBlogById(Long id) {
        Blog blog = this.getById(id);
        User loginUser = userService.getLoginUser();
        return this.getBlogVO(blog, loginUser);
    }

    @Override
    public PageResult<BlogVO> getBlogPage(BlogPageReqDTO blogPageReqDTO) {
        PageResult<Blog> blogPage = blogMapper.getBlogPage(blogPageReqDTO);
        List<Blog> list = blogPage.getList();
        if (list == null || list.isEmpty()) {
            return PageResult.empty();
        }
        User loginUser = userService.getLoginUser();
        List<BlogVO> blogList = list.stream().map(blog -> getBlogVO(blog, loginUser)).toList();
        return new PageResult<>(blogList, blogPage.getTotal());
    }


    private BlogVO getBlogVO(Blog blog, User loginUser) {
        BlogVO blogVO = new BlogVO();
        // 避免意外属性拷贝
        BeanUtil.copyProperties(blog, blogVO, "userInfo", "hasThumb");

        // 提取用户信息设置逻辑
        setAuthorInfo(blogVO);

        // 分离关注点：登录用户相关处理
        handleLoginUserSpecifics(blogVO, blog, loginUser);

        return blogVO;
    }

    private void setAuthorInfo(BlogVO blogVO) {
        Optional.ofNullable(blogVO.getUserId())
                .map(userId -> userService.getUserInfoById(userId))
                .map(userService::getSimpleUserVO)
                .ifPresent(blogVO::setUserInfo);
    }

    private void handleLoginUserSpecifics(BlogVO blogVO, Blog blog, User loginUser) {
        if (loginUser != null) {
            setThumbStatus(blogVO, blog, loginUser.getId());
        }
    }

    private void setThumbStatus(BlogVO blogVO, Blog blog, Long loginUserId) {
        boolean hasThumb = thumbService.lambdaQuery()
                .eq(Thumb::getUserId, loginUserId)
                .eq(Thumb::getBlogId, blog.getId())
                .count() > 0;

        blogVO.setHasThumb(hasThumb);
    }


}




