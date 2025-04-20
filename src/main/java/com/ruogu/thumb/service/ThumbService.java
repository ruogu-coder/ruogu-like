package com.ruogu.thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruogu.thumb.model.dto.thumb.ThumbLikeOrUnLikeDTO;
import com.ruogu.thumb.model.entity.Thumb;

/**
* @author ruogu
* @description 针对表【thumb(点赞记录表)】的数据库操作Service
* @createDate 2025-04-18 13:36:53
*/
public interface ThumbService extends IService<Thumb> {

    /**
     * 点赞
     * @param thumbLikeOrUnLikeDTO 请求对象
     * @return {@link Boolean }
     */
    Boolean doThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO);


    /**
     * 取消点赞
     * @param thumbLikeOrUnLikeDTO 请求对象
     * @return {@link Boolean }
     */
    Boolean undoThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO);


    /**
     * 是否点赞
     * @param userId 用户id
     * @param blogId 博客id
     * @return {@link Boolean }
     */
    Boolean isThumb(Long userId, Long blogId);

}
