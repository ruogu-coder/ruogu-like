package com.ruogu.thumb.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruogu.thumb.common.exception.ServiceException;
import com.ruogu.thumb.common.pojo.PageResult;
import com.ruogu.thumb.mapper.UserMapper;
import com.ruogu.thumb.model.dto.user.*;
import com.ruogu.thumb.model.entity.User;
import com.ruogu.thumb.model.enums.UserStatusEnum;
import com.ruogu.thumb.model.vo.user.LoginUserVO;
import com.ruogu.thumb.model.vo.user.UserSimpleVo;
import com.ruogu.thumb.model.vo.user.UserVo;
import com.ruogu.thumb.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruogu.thumb.common.exception.enums.GlobalErrorCodeConstants.*;
import static com.ruogu.thumb.common.exception.util.ServiceExceptionUtil.exception;

/**
* @author ruogu
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-04-18 13:37:22
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    /**
     * 盐值，用于混淆密码
     */
    public static final String SALT = "codeZhang";

    @Resource
    private UserMapper userMapper;

    /**
     * 注册用户
     *
     * @param userAccount   用户账号，用于标识一个用户。
     * @param userPassword  用户密码，用于用户身份验证。
     * @param checkPassword 确认密码，用于验证用户输入的密码是否一致。
     * @return 返回注册结果，通常情况下，成功返回一个用户id。
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 校验输入参数
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw exception(BAD_REQUEST);
        }
        if (userAccount.length() < 4) {
            throw exception(BAD_REQUEST);
        }
        // 密码和确认密码是否相同
        if (!userPassword.equals(checkPassword)) {
            throw exception(PASSWORD_NOT_MATCH);
        }
        if (userPassword.length() < 8) {
            throw exception(PASSWORD_LENGTH_NOT_ENOUGH);
        }
        synchronized (userAccount.intern()) {
            // 账号重复性检查
            accountReportCheck(userAccount);
            // 对密码进行加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 创建新用户并保存
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw exception(USER_REGISTER_FAIL);
            }
            return user.getId();
        }
    }

    /**
     * 账号重复性检查
     *
     * @param userAccount 用户账号
     */
    private void accountReportCheck(String userAccount) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw exception(USER_NAME_REPEAT);
        }
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账号。
     * @param userPassword 用户密码。
     * @return 登录成功返回用户信息的VO对象。
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword) {
        // 校验输入参数
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw exception(BAD_REQUEST);
        }
        if (userAccount.length() < 4) {
            throw exception(BAD_REQUEST);
        }
        if (userPassword.length() < 8) {
            throw exception(BAD_REQUEST);
        }
        // 对密码进行加密并验证用户是否存在
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = this.baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));
        if (user == null) {
            throw exception(USER_NOT_EXIST);
        }
        if (!user.getUserPassword().equals(encryptPassword)) {
            throw exception(PASSWORD_ERROR);
        }
        if (!UserStatusEnum.isNormal(user.getUserStatus())) {
            throw exception(USER_NOT_NORMAL);
        }
        StpUtil.login(user.getId());
        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录的用户
     *
     * @return 如果用户已登录，返回登录的用户对象；否则返回null。
     */
    @Override
    public User getLoginUser() {
        if (StpUtil.isLogin()) {
            return this.getById(StpUtil.getLoginIdAsLong());
        }
        return null;
    }

    /**
     * 将用户对象转换为登录用户VO对象
     *
     * @param user 用户对象
     * @return 返回登录用户VO对象
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            throw exception(UNAUTHORIZED);
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    /**
     * 添加新用户
     *
     * @param userReqDTO 用户信息请求DTO
     * @return 添加成功返回用户id
     */
    @Override
    public long addUser(UserSaveReqDTO userReqDTO) {
        // 校验输入参数
        if (StringUtils.isAnyBlank(userReqDTO.getUserAccount(), userReqDTO.getUserPassword(), userReqDTO.getUserName())) {
            throw exception(BAD_REQUEST);
        }
        User user = new User();
        BeanUtil.copyProperties(userReqDTO, user);
        String userAccount = userReqDTO.getUserAccount();
        String userPassword = userReqDTO.getUserPassword();
        synchronized (userAccount.intern()) {
            // 账号重复性检查
            accountReportCheck(userAccount);
            // 对密码进行加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw exception(USER_REGISTER_FAIL);
            }
            return user.getId();
        }
    }

    /**
     * 更新用户信息
     *
     * @param userReqDTO 用户信息更新请求DTO
     * @return 更新成功返回true
     */
    @Override
    public boolean updateUser(UserSaveReqDTO userReqDTO) {
        // 校验输入参数
        if (StringUtils.isAnyBlank(userReqDTO.getUserAccount(), userReqDTO.getUserName())) {
            throw exception(BAD_REQUEST);
        }
        if (userReqDTO.getId() == null) {
            throw exception(BAD_REQUEST);
        }
        User user = new User();
        BeanUtil.copyProperties(userReqDTO, user, "userPassword");
        // 判断用户账号是否已存在
        if (this.baseMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, user.getUserAccount())
                .ne(User::getId, user.getId())) > 0) {
            throw exception(USER_NAME_REPEAT);
        }
        boolean b = this.updateById(user);
        if (!b) {
            throw exception(UPDATE_FAIL);
        }
        return true;
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 删除成功返回true
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean deleteUser(Long id) {
        if (id == null) {
            throw exception(BAD_REQUEST);
        }
        boolean b = this.removeById(id);
        if (!b) {
            throw exception(DELETE_FAIL);
        }
        return true;
    }

    /**
     * 将用户对象转换为用户VO对象
     *
     * @param user 用户对象
     * @return 返回用户VO对象
     */
    @Override
    public UserSimpleVo getSimpleUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserSimpleVo userSimpleVo = new UserSimpleVo();
        BeanUtil.copyProperties(user, userSimpleVo);
        return userSimpleVo;
    }

    /**
     * 获取用户分页信息
     *
     * @param userPageReqDTO 用户分页请求DTO
     * @return 返回用户分页结果
     */
    @Override
    public PageResult<UserVo> getUserPage(UserPageReqDTO userPageReqDTO) {
        PageResult<User> pageResult = userMapper.selectPage(userPageReqDTO);
        if (pageResult.getList() == null) {
            return PageResult.empty();
        }
        List<UserVo> userVos = pageResult.getList().stream().map(user -> {
            UserVo userVo = new UserVo();
            BeanUtil.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        return new PageResult<>(userVos, pageResult.getTotal());
    }

    /**
     * 更新用户个人资料
     *
     * @param userProfileUpdateReqDTO 用户个人资料更新请求DTO
     * @return 更新成功返回true
     */
    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean updateUserProfile(UserProfileUpdateReqDTO userProfileUpdateReqDTO) {
        if (userProfileUpdateReqDTO == null) {
            throw exception(BAD_REQUEST);
        }
        long userId = StpUtil.getLoginIdAsLong();
        User user = new User();
        user.setId(userId);
        BeanUtil.copyProperties(userProfileUpdateReqDTO, user);
        if (this.updateById(user)) {
            return true;
        }
        throw exception(UPDATE_FAIL);
    }

    @Override
    public UserVo getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public boolean resetUserPassword(UserPasswordResetReqDTO userPasswordResetReqDTO) {
        String userId = userPasswordResetReqDTO.getId();
        String newPassword = userPasswordResetReqDTO.getNewPassword();
        if (StringUtils.isAnyBlank(userId, newPassword)) {
            throw exception(BAD_REQUEST_PARAMS_ERROR, "用户id或者密码为空");
        }
        User user = this.getById(userId);
        if (user == null) {
            throw exception(USER_NOT_EXIST);
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        user.setUserPassword(encryptPassword);
        return this.updateById(user);
    }

    @Override
    public Boolean updatePassword(UserPasswordUpdateReqDTO userPasswordUpdateReqDTO) {
        String oldPassword = userPasswordUpdateReqDTO.getOldPassword();
        String newPassword = userPasswordUpdateReqDTO.getNewPassword();
        String checkPassword = userPasswordUpdateReqDTO.getCheckPassword();
        if (StringUtils.isAnyBlank(oldPassword, newPassword, checkPassword)) {
            throw exception(BAD_REQUEST_PARAMS_ERROR, "请求参数缺失，请检查参数！");
        }
        long loginUserId = StpUtil.getLoginIdAsLong();
        User user = this.getById(loginUserId);
        if (!DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes()).equals(user.getUserPassword())) {
            throw exception(USER_PASSWORD_ERROR);
        }
        if (!newPassword.equals(checkPassword)) {
            throw exception(PASSWORD_NOT_MATCH);
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean updated = this.updateById(user);
        if (!updated) {
            throw exception(UPDATE_FAIL);
        }
        return true;
    }

    @Override
    public User getUserInfoById(Long userId) {
        return this.getById(userId);
    }

    @Override
    public Map<Long, User> getUserMapByIds(Collection<Long> collect) {
        if (CollectionUtils.isEmpty(collect)) {
            return Collections.emptyMap();
        }
        List<User> users = this.listByIds(collect);
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }
}




