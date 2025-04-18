package com.ruogu.thumb.controller;

import com.ruogu.thumb.service.BlogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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





}
