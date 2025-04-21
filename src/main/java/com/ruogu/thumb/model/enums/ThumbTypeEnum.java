package com.ruogu.thumb.model.enums;

import lombok.Getter;

/**
 * @Author code_zhang
 * @Date 2025/4/21 22:49
 */
@Getter
public enum ThumbTypeEnum {
    // 点赞
    INCR(1),
    // 取消点赞
    DECR(-1),
    // 不发生改变
    NON(0),
    ;

    private final int value;

    ThumbTypeEnum(int value) {
        this.value = value;
    }
}
