package com.oriseq.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.service.IUserGroupService;
import com.oriseq.service.IUsersService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Description: 登录工具
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/30 14:31
 */
@Component
public class LoginTool {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IUsersService usersService;
    @Autowired
    private IUserGroupService userGroupService;

    /**
     * 过期时间
     */
    private Long expireTime = 1L;

    @NotNull
    private static LoginUser getLoginUserAndSetProp(String token, User one, UserGroup byId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setToken(token);
        loginUser.setUserId(one.getId().toString());
        loginUser.setUsername(one.getUsername());
        loginUser.setPhone(one.getPhoneNumber());
        loginUser.setUserGroupId(one.getUserGroupId());
        loginUser.setIsSuper(one.getIsSuper());
        // 组信息
        loginUser.setUserGroupName(Optional.ofNullable(byId).map(UserGroup::getGroupName).orElse(""));
        loginUser.setIsGroupSuper(one.getIsGroupSuper());
        loginUser.setAvailStatusGroup(byId.getAvailStatus());
        loginUser.setIsInternalGroup(byId.getIsInternalGroup());
        loginUser.setRealName(one.getRealName());
        return loginUser;
    }

    /**
     * 用户信息
     *
     * @return
     */
    public LoginUser getUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginUser userInfo = getUserInfo(request);
        return userInfo;
    }

    /**
     * 先从缓存中取，没有就从数据库中取
     * 目的避免短时间的访问压力
     *
     * @param request
     * @return
     */
    public LoginUser getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("token出错");
        }
        // 使用了phone作为唯一不空值
        String phone = JwtUtils.getUsernameFromToken(token);
//        String jsonUser = redisTemplate.opsForValue().get(phone);
//        if (StringUtils.isBlank(jsonUser)) {
//            // 查询设置用户信息
//            User one = usersService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, phone));
//            // 查询用户组
//            UserGroup byId = userGroupService.getById(one.getUserGroupId());
//            LoginUser loginUser = getLoginUserAndSetProp(token, one, byId);
//            // 1分钟，避免短时间的访问压力
//            redisTemplate.opsForValue().set(phone, new Gson().toJson(loginUser), expireTime, TimeUnit.MINUTES);
//            return loginUser;
//        }
//        LoginUser loginUser = new Gson().fromJson(jsonUser, LoginUser.class);
        // 直接从数据库中取
        // 查询设置用户信息
        User one = usersService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, phone));
        // 查询用户组
        UserGroup byId = userGroupService.getById(one.getUserGroupId());
        LoginUser loginUser = getLoginUserAndSetProp(token, one, byId);
        return loginUser;
    }

    /**
     * 设置缓存
     *
     * @param token
     * @return
     */
    public LoginUser setUserInfo(String token) {
        String phone = JwtUtils.getUsernameFromToken(token);
        // 查询设置用户信息
        User one = usersService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, phone));
        // 查询用户组
        UserGroup byId = userGroupService.getById(one.getUserGroupId());
        LoginUser loginUser = getLoginUserAndSetProp(token, one, byId);
        // 1天过期
        redisTemplate.opsForValue().set(phone, new Gson().toJson(loginUser), expireTime, TimeUnit.MINUTES);
        return loginUser;
    }

    /**
     * 删除用户缓存
     *
     * @return
     */
    public Boolean deleteUserInfoCache(String phone) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String token = request.getHeader("Authorization");
//        if (StringUtils.isBlank(token)) {
//            throw new RuntimeException("token出错");
//        }
//        // 使用了phone作为唯一不空值
//        String phone = JwtUtils.getUsernameFromToken(token);
        // 删除缓存
        Boolean deleteSta = redisTemplate.delete(phone);
        return deleteSta;
    }


    public boolean isSuper(LoginUser loginUser) {
        if (Optional.ofNullable(loginUser.getIsSuper()).orElse(0) == 1) {
            return true;
        }
        return false;
    }

    public boolean isSuper() {
        LoginUser userInfo = this.getUserInfo();
        return userInfo.isSuper();
    }

    public boolean isGroupSuper(LoginUser loginUser) {
        if (Optional.ofNullable(loginUser.getIsGroupSuper()).orElse(0) == 1) {
            return true;
        }
        return false;
    }

    public boolean isGroupSuper() {
        LoginUser userInfo = this.getUserInfo();
        return userInfo.isGroupSuper();
    }


}
