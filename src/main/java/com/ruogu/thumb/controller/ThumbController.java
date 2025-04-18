package com.ruogu.thumb.controller;

import com.ruogu.thumb.common.pojo.CommonResult;
import com.ruogu.thumb.model.dto.thumb.ThumbLikeOrUnLikeDTO;
import com.ruogu.thumb.service.ThumbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ruogu
 * @Date 2025/4/19 0:28
 */
@RestController
@RequestMapping("/thumb")
@Tag(name = "点赞")
public class ThumbController {

    @Resource
    private ThumbService thumbService;

    @PostMapping("/do")
    @Operation(summary = "点赞")
    public CommonResult<Boolean> doThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        return CommonResult.success(thumbService.doThumb(thumbLikeOrUnLikeDTO));
    }

    @PostMapping("/undo")
    @Operation(summary = "取消点赞")
    public CommonResult<Boolean> undoThumb(ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        return CommonResult.success(thumbService.undoThumb(thumbLikeOrUnLikeDTO));
    }



}
