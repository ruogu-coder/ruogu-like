package com.ruogu.thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.mapper.ThumbMapper;
import com.ruogu.thumb.model.dto.thumb.ThumbLikeOrUnLikeDTO;
import com.ruogu.thumb.model.entity.Blog;
import com.ruogu.thumb.model.entity.Thumb;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.service.BlogService;
import com.ruogu.thumb.service.ThumbService;
import com.ruogu.thumb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import static com.ruogu.thumb.common.exception.enums.GlobalErrorCodeConstants.*;
import static com.ruogu.thumb.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author ruogu
 * @description 针对表【thumb(点赞记录表)】的数据库操作Service实现
 * @createDate 2025-04-18 13:36:53
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    private final UserService userService;

    private final BlogService blogService;

    private final TransactionTemplate transactionTemplate;


    @Override
    public Boolean doThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        if (thumbLikeOrUnLikeDTO == null || thumbLikeOrUnLikeDTO.getBlogId() == null) {
            throw exception(BAD_REQUEST);
        }
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            throw exception(UNAUTHORIZED);
        }
        synchronized (loginUser.getId().toString().intern()) {
            return transactionTemplate.execute(status -> {
                Long blogId = thumbLikeOrUnLikeDTO.getBlogId();
                boolean exists = this.lambdaQuery()
                        .eq(Thumb::getUserId, loginUser.getId())
                        .eq(Thumb::getBlogId, blogId)
                        .exists();
                if (exists) {
                    throw exception(USER_LIKE_ERROR);
                }
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumb_count = thumb_count + 1")
                        .update();

                Thumb thumb = new Thumb();
                thumb.setUserId(loginUser.getId());
                thumb.setBlogId(blogId);
                return update && this.save(thumb);
            });
        }

    }



    @Override
    public Boolean undoThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        if (thumbLikeOrUnLikeDTO == null || thumbLikeOrUnLikeDTO.getBlogId() == null) {
            throw exception(BAD_REQUEST);
        }
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            throw exception(UNAUTHORIZED);
        }
        // 加锁
        synchronized (loginUser.getId().toString().intern()) {

            // 编程式事务
            return transactionTemplate.execute(status -> {
                Long blogId = thumbLikeOrUnLikeDTO.getBlogId();
                Thumb thumb = this.lambdaQuery()
                        .eq(Thumb::getUserId, loginUser.getId())
                        .eq(Thumb::getBlogId, blogId)
                        .one();
                if (thumb == null) {
                    throw exception(USER_UNLIKE_ERROR);
                }
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumb_count = thumb_count - 1")
                        .update();

                return update && this.removeById(thumb.getId());
            });
        }
    }
}




