package com.ruogu.thumb.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruogu.thumb.common.pojo.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户表
 *
 * @author ruogu
 * @TableName user
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user")
@Data
public class User extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 微信开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 用户状态（0正常 1停用）
     */
    private Integer userStatus;

}