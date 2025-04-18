package com.ruogu.thumb.model.vo.blog;

import com.ruogu.thumb.model.vo.user.UserSimpleVo;
import lombok.Data;

import java.util.Date;

/**
 * @Author ruogu
 * @Date 2025/4/18 19:05
 */
@Data
public class BlogVO {

    private Long id;

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
    private Date createTime;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建用户信息
     */
    private UserSimpleVo userInfo;

}

