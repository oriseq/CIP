package com.oriseq.config;

import com.oriseq.service.IUserGroupService;
import com.oriseq.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private IUsersService usersService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private IUserGroupService userGroupService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有接口
                .allowCredentials(true) // 是否发送 Cookie
                .allowedOriginPatterns("*") // 支持域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 支持方法
                .allowedHeaders("*");
//                .exposedHeaders("*");

    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        //排除拦截，除了注册登录(此时还没token)，其他都拦截
        excludePath.add("/regist");  //注册
//        excludePath.add("/test");  //TODO test
        excludePath.add("/login");     //登录
        excludePath.add("/smsLogin");     //短信登录
        excludePath.add("/smsVerificationCode");     //获取验证码
        excludePath.add("/verificationCode");     //获取验证码：重置密码
        excludePath.add("/resetPasswd");     //重置密码
        excludePath.add("/sysConfig/list");     //获取所有系统配置
        excludePath.add("/file/open/**");     //获取开放文件
        excludePath.add("/wx/login");  // 小程序
//        excludePath.add("/file/**");  // 关闭任意访问 文件相关
        excludePath.add("/static/**");  //静态资源
        excludePath.add("/assets/**");  //静态资源

        registry.addInterceptor(new TokenInterceptor(usersService, userGroupService, loginTool))
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }


}

