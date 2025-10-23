package com.oriseq.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oriseq.controller.exception.ErrorEnum;
import com.oriseq.controller.exception.TokenExpiresException;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.service.IUserGroupService;
import com.oriseq.service.IUsersService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;


@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private LoginTool loginTool;

    public TokenInterceptor(IUsersService usersService, IUserGroupService userGroupService, LoginTool loginTool) {
        this.usersService = usersService;
        this.userGroupService = userGroupService;
        this.loginTool = loginTool;
    }

//    public TokenInterceptor(LoginTool loginTool) {
//        this.loginTool = loginTool;
//    }

//    public TokenInterceptor(IUsersService usersService) {
//        this.usersService = usersService;
//    }

    /**
     * 无权限返回错误
     *
     * @param response
     * @param result
     * @param errorEnum 错误信息枚举
     * @return
     * @throws IOException
     */
    private static boolean errorRsp(HttpServletResponse response, Boolean result, ErrorEnum errorEnum) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            Result<Object> objectResult = new Result<>();
            objectResult.setMessage(errorEnum.getErrorMsg());
            objectResult.setCode(errorEnum.getErrorCode());
            objectResult.setSuccess(result);
            // 转json串
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(objectResult);
            response.getWriter().append(s);
            log.info("认证失败，未通过拦截器");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
            return false;
        }
        return false;
    }

    //    private static IUsersService userService = new UsersServiceImpl();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Authorization");
        Boolean result = false;
        if (token != null) {
            result = JwtUtils.validateToken(token);
            String phone = null;
            try {
                phone = JwtUtils.getUsernameFromToken(token);
            } catch (ExpiredJwtException e) {
                log.info("认证失败，token过期");
                throw new TokenExpiresException(401, "登录过期");
            }

//            LoginUser userInfo = loginTool.getUserInfo(request);
            // 1.token和数据库用户
            if (result) {
                // 加上数据库用户验证
                User one = usersService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, phone));
                if (one == null) {
                    throw new TokenExpiresException(401, "登录过期");
                }
                // 判断禁用逻辑
                // 超级管理员权限最高，无法禁用
                if (one.getIsSuper() != 1) {
                    // 判断用户状态
                    if (!one.getAvail()) {
                        return errorRsp(response, result, ErrorEnum.NO_PERMISSION);
                    }
                    // 用户组状态
                    UserGroup byId = userGroupService.getById(one.getUserGroupId());
                    Boolean availStatusGroup = byId.getAvailStatus();
                    if (!availStatusGroup) {
                        return errorRsp(response, result, ErrorEnum.NO_PERMISSION);
                    }
                }
                log.debug("通过拦截器");
                return true;
            }

        }
        return errorRsp(response, result, ErrorEnum.NO_AUTH);
    }
}

