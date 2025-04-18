package com.ruogu.thumb.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 博客表
 *
 * @author ruogu
 * @TableName blog
 */
@TableName(value = "blog")
@Data
public class Blog {
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
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String coverImg;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbCount;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;


}