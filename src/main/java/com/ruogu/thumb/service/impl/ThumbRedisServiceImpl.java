package com.ruogu.thumb.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.constant.RedisLuaScriptConstant;
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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

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
public class ThumbRedisServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;


    private String getTimeSlice() {
        DateTime nowDate = DateUtil.date();
        // 获取当前时间最近的整数秒 例如: 10:05:03 -> 10:05:00
        return DateUtil.format(nowDate, "HH:mm") + (DateUtil.second(nowDate) / 10) * 10;
    }

    private User validateRequestAndGetUser(ThumbLikeOrUnLikeDTO dto) {
        if (dto == null || dto.getBlogId() == null) {
            throw exception(BAD_REQUEST);
        }
        return Optional.ofNullable(userService.getLoginUser())
                .orElseThrow(() -> exception(UNAUTHORIZED));
    }

    @Override
    public Boolean doThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        User loginUser = validateRequestAndGetUser(thumbLikeOrUnLikeDTO);
        Long blogId = thumbLikeOrUnLikeDTO.getBlogId();
        // 获取时间
        String timeSlice = getTimeSlice();
        // 获取Redis的key
        String userThumbKey = RedisKeyUtil.getUserInfoKey(loginUser.getId());
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(timeSlice);

        // 执行Lua脚本
        long result = redisTemplate.execute(
                RedisLuaScriptConstant.THUMB_SCRIPT,
                Arrays.asList(tempThumbKey, userThumbKey),
                loginUser.getId(),
                blogId,
                DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss")
        );
        if (LuaStatusEnum.FAIL.getValue() == result) {
            throw exception(USER_LIKE_ERROR);
        }
        return LuaStatusEnum.SUCCESS.getValue() == result;
    }


    @Override
    public Boolean undoThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        User loginUser = validateRequestAndGetUser(thumbLikeOrUnLikeDTO);
        Long blogId = thumbLikeOrUnLikeDTO.getBlogId();
        // 获取时间
        String timeSlice = getTimeSlice();
        // 获取Redis的key
        String userThumbKey = RedisKeyUtil.getUserInfoKey(loginUser.getId());
        String tempThumbKey = RedisKeyUtil.getTempThumbKey(timeSlice);

        // 执行Lua脚本
        long result = redisTemplate.execute(
                RedisLuaScriptConstant.UNTHUMB_SCRIPT,
                Arrays.asList(tempThumbKey, userThumbKey),
                loginUser.getId(),
                blogId
        );
        if (LuaStatusEnum.FAIL.getValue() == result) {
            throw exception(USER_UNLIKE_ERROR);
        }
        return LuaStatusEnum.SUCCESS.getValue() == result;
    }

    @Override
    public Boolean isThumb(Long userId, Long blogId) {
        return redisTemplate.opsForHash().hasKey(RedisKeyUtil.getUserThumbKey(userId), blogId.toString());
    }
}




