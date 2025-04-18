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

    private static final String FIELD_UPDATE_TIME_CAPITAL = "updateTime";


    @Override
    public void insertFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("createTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("createTime", DateUtil.date(), metaObject);
        }
        Object modifyTime1 = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime1)) {
            setFieldValByName("updateTime", DateUtil.date(), metaObject);
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
        // 校验 metaObject 是否为 null，避免空指针异常
        if (metaObject == null) {
            throw new IllegalArgumentException("metaObject cannot be null");
        }
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName(FIELD_UPDATE_TIME_CAPITAL, metaObject);
        // 如果 modifyTime 不为空，则设置 "updateTime" 字段值
        if (Objects.nonNull(modifyTime)) {
            try {
                setFieldValByName(FIELD_UPDATE_TIME_CAPITAL, DateUtil.date(), metaObject);
            } catch (Exception e) {
                // 捕获异常并记录日志，防止程序崩溃
                System.err.println("Failed to set field 'updateTime': " + e.getMessage());
                e.printStackTrace();
            }
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