package com.ruogu.thumb.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 点赞记录表
 * @author ruogu
 * @TableName thumb
 */
@TableName(value ="thumb")
@Data
public class Thumb {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 博客编号
     */
    private Long blogId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}