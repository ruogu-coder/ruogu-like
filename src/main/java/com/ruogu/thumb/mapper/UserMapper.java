package com.ruogu.thumb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.core.mapper.BaseMapperPlus;
import com.ruogu.thumb.model.dto.user.UserPageReqDTO;
import com.ruogu.thumb.model.entity.User;

import java.util.Objects;

/**
 * @author ruogu
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2025-04-18 13:37:22
 * @Entity com.ruogu.thumb.model.entity.User
 */
public interface UserMapper extends BaseMapperPlus<User> {

    /**
     * 定义一个默认方法selectPage，用于分页查询用户信息
     *
     * @param userPageReqDTO 查询对象
     * @return 分页用户信息
     */
    default PageResult<User> selectPage(UserPageReqDTO userPageReqDTO) {
        return selectPage(userPageReqDTO, new LambdaQueryWrapper<User>()
                .eq(Objects.nonNull(userPageReqDTO.getId()), User::getId, userPageReqDTO.getId())
                .eq(Objects.nonNull(userPageReqDTO.getUserName()), User::getUserName, userPageReqDTO.getUserName())
                .eq(Objects.nonNull(userPageReqDTO.getUserRole()), User::getUserRole, userPageReqDTO.getUserRole())
                .eq(Objects.nonNull(userPageReqDTO.getMpOpenId()), User::getMpOpenId, userPageReqDTO.getMpOpenId())
                .eq(Objects.nonNull(userPageReqDTO.getUnionId()), User::getUnionId, userPageReqDTO.getUnionId()));
    }

}




