package com.ruogu.thumb.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 13:51
 **/
@Data
@Schema(description = "用户修改密码请求")
public class UserPasswordUpdateReqDTO {

    @Schema(description = "旧密码")
    private String oldPassword;

    @Schema(description = "新密码")
    private String newPassword;

    @Schema(description = "确认密码")
    private String checkPassword;


}