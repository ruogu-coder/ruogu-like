package com.ruogu.thumb.model.dto.thumb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author ruogu
 * @Date 2025/4/18 20:33
 */
@Data
@Schema(description = "点赞或取消点赞参数")
public class ThumbLikeOrUnLikeDTO {

    @Schema(description = "博客id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long blogId;

}
