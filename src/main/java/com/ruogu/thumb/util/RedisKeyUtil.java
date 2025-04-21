package com.ruogu.thumb.util;

import com.ruogu.thumb.constant.ThumbConstant;

/**
 * @Author ruogu
 * @Date 2025/4/20 11:51
 */
public class RedisKeyUtil {

    /**
     * 获取用户点赞key
     * @param userId 用户id
     * @return 缓存key
     */
    public static String getUserThumbKey(Long userId) {
        return ThumbConstant.USER_THUMB_KEY_PREFIX + userId;
    }

    /**
     * 获取用户信息key
     * @param userId 用户id
     * @return 缓存key
     */
    public static String getUserInfoKey(Long userId) {
        return ThumbConstant.USER_INFO_KEY_PREFIX + userId;
    }


    /**
     * 获取 临时点赞记录 key
     * @param time 时间
     * @return 缓存key
     */
    public static String getTempThumbKey(String time) {
        return ThumbConstant.TEMP_THUMB_KEY_PREFIX.formatted(time);
    }


}