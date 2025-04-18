package com.ruogu.thumb.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Objects;

/***
 *@author ruogu
 *@create 2025/4/18 11:40
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("create_time", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("create_time", DateUtil.date(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("creator", metaObject);
        if (Objects.nonNull(modifier)) {
            long userId;
            try {
                userId = StpUtil.getLoginIdAsLong();
            } catch (Exception e) {
                userId = 1L;
            }
            setFieldValByName("creator", Long.toString(userId), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("update_time", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", DateUtil.date(), metaObject);
        }
        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updater", metaObject);
        if (Objects.nonNull(modifier)) {
            long userId;
            try {
                userId = StpUtil.getLoginIdAsLong();
            } catch (Exception e) {
                userId = 1L;
            }
            setFieldValByName("updater", Long.toString(userId), metaObject);
        }

    }

}