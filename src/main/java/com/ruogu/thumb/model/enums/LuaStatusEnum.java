package com.ruogu.thumb.model.enums;

import lombok.Getter;

/**
 * @Author code_zhang
 * @Date 2025/4/21 22:51
 */
@Getter
public enum LuaStatusEnum {
    // 成功
    SUCCESS(1L),
    // 失败
    FAIL(-1L),
    ;

    private final long value;

    LuaStatusEnum(long value) {
        this.value = value;
    }
}
