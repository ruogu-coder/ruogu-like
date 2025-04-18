package com.ruogu.thumb.common.exception;

import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 11:36
 **/
@Data
public class ErrorCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }


}