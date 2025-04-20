package com.ruogu.thumb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.mapper.BlogMapper;
import com.ruogu.thumb.model.dto.blog.BlogPageReqDTO;
import com.ruogu.thumb.model.entity.Blog;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.model.vo.blog.BlogVO;
import com.ruogu.thumb.service.BlogService;
import com.ruogu.thumb.service.ThumbService;
import com.ruogu.thumb.service.UserService;
import com.ruogu.thumb.util.RedisKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


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
        if (loginUser == null) {
            return new PageResult<>(copyBlogListToVO(list, null, null), blogPage.getTotal());
        }

        List<Object> blogIds = list.stream()
                .map(blog -> String.valueOf(blog.getId()))
                .collect(Collectors.toList());

        Map<Long, Boolean> thumbMap = getThumbStatusMap(loginUser.getId(), blogIds);

        Set<Long> userIds = new HashSet<>(list.size());
        // 去重用户ID并批量查询
        list.forEach(blog -> userIds.add(blog.getUserId()));
        Map<Long, User> userMap = userService.getUserMapByIds(userIds);
        return new PageResult<>(copyBlogListToVO(list, thumbMap, userMap), blogPage.getTotal());
    }

    private Map<Long, Boolean> getThumbStatusMap(Long userId, List<Object> blogIds) {
        Map<Long, Boolean> result = new HashMap<>(blogIds.size());
        List<Boolean> thumbs = redisTemplate.opsForHash()
                .multiGet(RedisKeyUtil.getUserThumbKey(userId), blogIds)
                .stream()
                .map(Objects::nonNull)
                .toList();

        for (int i = 0; i < thumbs.size(); i++) {
            result.put(Long.parseLong(String.valueOf(blogIds.get(i))), thumbs.get(i));
        }
        return result;
    }

    private List<BlogVO> copyBlogListToVO(List<Blog> blogs, Map<Long, Boolean> thumbMap, Map<Long, User> userMap) {
        return blogs.stream().map(blog -> {
            BlogVO vo = new BlogVO();
            vo.setId(blog.getId());
            vo.setTitle(blog.getTitle());
            vo.setCoverImg(blog.getCoverImg());
            vo.setContent(blog.getContent());
            vo.setThumbCount(blog.getThumbCount());
            vo.setCreateTime(blog.getCreateTime());
            vo.setUserId(blog.getUserId());
            // 增强空安全判断
            boolean hasThumb = thumbMap != null
                    ? thumbMap.getOrDefault(blog.getId(), false)
                    : false;
            vo.setHasThumb(hasThumb);

            if (userMap != null) {
                User user = userMap.getOrDefault(blog.getUserId(), new User());
                vo.setUserInfo(userService.getSimpleUserVO(user));
            }
            return vo;
        }).collect(Collectors.toList());
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
        Boolean exist = thumbService.isThumb(blog.getId(), loginUserId);
        blogVO.setHasThumb(exist);
    }


}




