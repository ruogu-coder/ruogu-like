package com.ruogu.thumb.model.dto.thumb;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ruogu.thumb.model.enums.ThumbTypeEnum;
import lombok.Data;

/***
 *@author ruogu
 *@create 2025/4/22 11:37
 **/
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class ThumbTempCacheDTO {

    /**
     * 类型
     * {@link ThumbTypeEnum }
     */
    private Integer type;

    /**
     * 点赞/取消点赞时间
     */
    private String time;

}
