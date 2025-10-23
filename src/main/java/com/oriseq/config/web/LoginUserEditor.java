//package com.oriseq.config.web;
//
//import com.oriseq.config.LoginTool;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.beans.PropertyEditorSupport;
//
//public class LoginUserEditor extends PropertyEditorSupport {
//    private final HttpServletRequest request;
//    private LoginTool loginTool;
//
//    public LoginUserEditor(HttpServletRequest request, LoginTool loginTool) {
//        this.request = request;
//        this.loginTool = loginTool;
//    }
//
//    @Override
//    public void setAsText(String text) throws IllegalArgumentException {
//        setValue(loginTool.getUserInfo(request));
//    }
//}