package com.ruogu.thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.model.entity.Blog;
import com.ruogu.thumb.service.BlogService;
import com.ruogu.thumb.mapper.BlogMapper;
import org.springframework.stereotype.Service;

/**
* @author ruogu
* @description 针对表【blog(博客表)】的数据库操作Service实现
* @createDate 2025-04-18 13:32:05
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService{

}




