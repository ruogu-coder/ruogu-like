package com.ruogu.thumb.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 13:51
 **/
@Data
@Schema(description = "重置用户密码请求")
public class UserPasswordResetReqDTO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "新密码")
    private String newPassword;
}