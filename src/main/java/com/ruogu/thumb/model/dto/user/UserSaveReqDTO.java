package com.ruogu.thumb.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/18 14:02
 **/
@Data
@Schema(description = "用户创建/更新请求")
@Valid
public class UserSaveReqDTO {

    @Schema(description = "id" )
    @NotBlank(message = "id不能为空")
    private Long id;

    @NotEmpty(message = "账号不能为空")
    @Schema(description = "账号")
    private String userAccount;

    @NotEmpty(message = "密码不能为空")
    @Schema(description = "密码")
    private String userPassword;

    @NotNull(message = "微信开放平台id不能为空")
    @Schema(description = "微信开放平台id",requiredMode = Schema.RequiredMode.REQUIRED)
    private String unionId;

    @NotNull(message = "公众号openId不能为空")
    @Schema(description = "公众号openId",requiredMode = Schema.RequiredMode.REQUIRED)
    private String mpOpenId;

    @NotNull(message = "用户昵称不能为空")
    @Schema(description = "用户昵称",requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;

    @NotNull(message = "用户头像不能为空")
    @Schema(description = "用户头像",requiredMode = Schema.RequiredMode.REQUIRED)
    private String userAvatar;

    @NotNull(message = "用户简介不能为空")
    @Schema(description = "用户简介",requiredMode = Schema.RequiredMode.REQUIRED)
    private String userProfile;

    @NotEmpty(message = "用户角色：user/admin不能为空")
    @Schema(description = "用户角色：user/admin")
    private String userRole;

    @Schema(description = "用户状态（0正常 1停用）")
    private Integer userStatus;

}