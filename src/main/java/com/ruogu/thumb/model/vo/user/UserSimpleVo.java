package com.ruogu.thumb.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 14:00
 **/
@Data
public class UserSimpleVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "用户简介")
    private String userProfile;

}
