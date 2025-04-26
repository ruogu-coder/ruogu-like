package com.ruogu.thumb.controller;

import com.ruogu.thumb.common.pojo.CommonResult;
import com.ruogu.thumb.model.dto.thumb.ThumbLikeOrUnLikeDTO;
import com.ruogu.thumb.service.ThumbService;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    private final Counter successCounter;
    private final Counter failureCounter;

    public ThumbController(MeterRegistry registry) {
        this.successCounter = Counter.builder("thumb.success.count")
                .description("Total successful thumb")
                .register(registry);
        this.failureCounter = Counter.builder("thumb.failure.count")
                .description("Total failed thumb")
                .register(registry);
    }

    @PostMapping("/do")
    @Operation(summary = "点赞")
    public CommonResult<Boolean> doThumb(@RequestBody ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        Boolean success;
        try {
            success = thumbService.doThumb(thumbLikeOrUnLikeDTO);
            if (success) {
                successCounter.increment();
            } else {
                failureCounter.increment();
            }
        } catch (Exception e) {
            failureCounter.increment();
            throw e;
        }
        return CommonResult.success(success);
    }

    @PostMapping("/undo")
    @Operation(summary = "取消点赞")
    public CommonResult<Boolean> undoThumb(@RequestBody ThumbLikeOrUnLikeDTO thumbLikeOrUnLikeDTO) {
        return CommonResult.success(thumbService.undoThumb(thumbLikeOrUnLikeDTO));
    }



}
