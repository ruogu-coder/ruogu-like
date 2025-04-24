package com.ruogu.thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.constant.RedisLuaScriptConstant;
import com.ruogu.thumb.listener.thumb.msg.ThumbEvent;
import com.ruogu.thumb.mapper.ThumbMapper;
import com.ruogu.thumb.model.dto.thumb.ThumbLikeOrUnLikeDTO;
import com.ruogu.thumb.model.entity.Thumb;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.model.enums.LuaStatusEnum;
import com.ruogu.thumb.service.ThumbService;
import com.ruogu.thumb.service.UserService;
import com.ruogu.thumb.util.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.ruogu.thumb.common.exception.enums.GlobalErrorCodeConstants.*;
import static com.ruogu.thumb.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @author ruogu
 * @description 针对表【thumb(点赞记录表)】的数据库操作Service实现
 * @createDate 2025-04-18 13:36:53
 */
@Service("thumbService")
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceMQImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final PulsarTemplate<ThumbEvent> pulsarTemplate;

    @Override
    public Boolean doThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        if (thumbLikeOrUnLikeDTO == null || thumbLikeOrUnLikeDTO.getBlogId() == null) {
            throw exception(BAD_REQUEST);
        }
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            throw exception(UNAUTHORIZED);
        }
        Long loginUserId = loginUser.getId();
        Long blogId = thumbLikeOrUnLikeDTO.getBlogId();
        String userThumbKey = RedisKeyUtil.getUserThumbKey(loginUserId);
        // 执行 Lua 脚本，点赞存入 Redis
        long result = redisTemplate.execute(
                RedisLuaScriptConstant.THUMB_SCRIPT_MQ,
                List.of(userThumbKey),
                blogId
        );
        if (LuaStatusEnum.FAIL.getValue() == result) {
            throw exception(USER_LIKE_ERROR);
        }

        ThumbEvent thumbEvent = ThumbEvent.builder()
                .blogId(blogId)
                .userId(loginUserId)
                .type(ThumbEvent.EventType.INCR)
                .eventTime(LocalDateTime.now())
                .build();
        pulsarTemplate.sendAsync("thumb-topic", thumbEvent).exceptionally(ex -> {
            redisTemplate.opsForHash().delete(userThumbKey, blogId.toString(), true);
            log.error("点赞事件发送失败: userId={}, blogId={}", loginUserId, blogId, ex);
            return null;
        });

        return true;
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
        Long loginUserId = loginUser.getId();
        Long blogId = thumbLikeOrUnLikeDTO.getBlogId();
        String userThumbKey = RedisKeyUtil.getUserThumbKey(loginUserId);
        // 执行 Lua 脚本，点赞记录从 Redis 删除
        long result = redisTemplate.execute(
                RedisLuaScriptConstant.UN_THUMB_SCRIPT_MQ,
                List.of(userThumbKey),
                blogId
        );
        if (LuaStatusEnum.FAIL.getValue() == result) {
            throw exception(USER_UNLIKE_ERROR);
        }
        ThumbEvent thumbEvent = ThumbEvent.builder()
                .blogId(blogId)
                .userId(loginUserId)
                .type(ThumbEvent.EventType.DECR)
                .eventTime(LocalDateTime.now())
                .build();
        pulsarTemplate.sendAsync("thumb-topic", thumbEvent).exceptionally(ex -> {
            redisTemplate.opsForHash().put(userThumbKey, blogId.toString(), true);
            log.error("点赞事件发送失败: userId={}, blogId={}", loginUserId, blogId, ex);
            return null;
        });

        return true;
    }

    @Override
    public Boolean isThumb(Long blogId, Long userId) {
        return redisTemplate.opsForHash().hasKey(RedisKeyUtil.getUserThumbKey(userId), blogId.toString());
    }



}





