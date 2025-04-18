package com.ruogu.thumb.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.ruogu.thumb.common.pojo.CommonResult;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.constant.UserConstant;
import com.ruogu.thumb.model.dto.blog.BlogPageReqDTO;
import com.ruogu.thumb.model.vo.blog.BlogVO;
import com.ruogu.thumb.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 *@author ruogu
 *@create 2025/4/18 17:20
 **/
@Tag(name = "管理后台-博客管理")
@RestController
@AllArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;


    @GetMapping("/getBlogVoById")
    @Operation(summary = "根据id获取博客信息")
    @Parameter(name = "id", description = "博客id",required = true)
    public CommonResult<BlogVO> getBlogById(Long id) {
        BlogVO blogVO = blogService.getBlogById(id);
        return CommonResult.success(blogVO);
    }
    @GetMapping("/page")
    @Operation(summary = "分页获取博客列表")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public CommonResult<PageResult<BlogVO>> getUserPage(BlogPageReqDTO blogPageReqDTO) {
        // 调用服务层方法，获取博客分页信息，并返回结果
        return CommonResult.success(blogService.getBlogPage(blogPageReqDTO));
    }


}
