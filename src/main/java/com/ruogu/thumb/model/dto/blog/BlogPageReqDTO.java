package com.ruogu.thumb.model.dto.blog;

import com.ruogu.thumb.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author ruogu
 * @Date 2025/4/18 20:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "分页获取博客列表参数")
public class BlogPageReqDTO extends PageParam implements Serializable {

    @Schema(description = "博客名称")
    private String blogName;


}
