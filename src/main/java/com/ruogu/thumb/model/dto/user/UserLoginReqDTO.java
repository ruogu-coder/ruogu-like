package com.ruogu.thumb.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 13:47
 **/
@Data
@Schema(description = "用户登录")
public class UserLoginReqDTO {

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userAccount;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userPassword;

}