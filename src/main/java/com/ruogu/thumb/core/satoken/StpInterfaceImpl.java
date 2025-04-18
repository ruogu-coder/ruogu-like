package com.ruogu.thumb.core.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/***
 *@author ruogu
 *@create 2025/4/18 14:21
 **/
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userService.getById((Serializable) loginId);
        if (user != null) {
            return Collections.singletonList(user.getUserRole());
        }
        return null;
    }
}