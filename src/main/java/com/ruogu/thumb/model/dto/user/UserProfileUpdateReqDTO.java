package com.ruogu.thumb.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 13:51
 **/
@Data
@Schema(description = "用户修改个人信息VO")
public class UserProfileUpdateReqDTO {

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "用户简介")
    private String userProfile;
}