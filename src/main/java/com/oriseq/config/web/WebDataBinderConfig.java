//package com.oriseq.config.web;
//
//import com.oriseq.config.LoginTool;
//import com.oriseq.dtm.dto.LoginUser;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.InitBinder;
//
//@ControllerAdvice
//public class WebDataBinderConfig {
//
//    @Autowired
//    private LoginTool loginTool;
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
//        binder.registerCustomEditor(LoginUser.class, new LoginUserEditor(request, loginTool));
//    }
//}