package com.ruogu.thumb.common.exception;

import com.ruogu.thumb.common.exception.enums.GlobalErrorCodeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/***
 *@author ruogu
 *@create 2025/4/18 11:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public final class ServiceException extends RuntimeException {
    /**
     * 业务错误码
     *
     * @see GlobalErrorCodeConstants
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public ServiceException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}