package com.ruogu.thumb.common.pojo;

import com.ruogu.thumb.common.exception.ErrorCode;
import com.ruogu.thumb.common.exception.ServiceException;
import com.ruogu.thumb.common.exception.enums.GlobalErrorCodeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;

/***
 *@author ruogu
 *@create 2025/4/18 11:25
 **/
@Data
@Tag(name = "基础响应对象")
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    @Schema(description = "业务状态", example = "0")
    private Integer code;
    /**
     * 返回数据
     */
    @Schema(description = "返回数据", example = "null")
    private T data;
    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    @Schema(description = "消息提示", example = "null")
    private String msg;


    public static <T> CommonResult<T> error( String message) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.FAIL.getCode();
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = GlobalErrorCodeConstants.SUCCESS.getMsg();
        return result;
    }

    public static <T> CommonResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

}
