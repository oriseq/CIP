package com.oriseq.config;

import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.service.IPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 权限工具
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/30 14:31
 */
@Component
public class PermissionTool {

    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private LoginTool loginTool;

    /**
     * 判断是否有权限
     *
     * @param permissionCode 权限码
     * @param request
     * @return
     */
    public boolean hasPermission(String permissionCode, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        boolean b = permissionService.hasPermission(permissionCode, userInfo);
        return b;
    }

    /**
     * 判断是否有权限
     *
     * @param permissionCode 权限码
     * @return
     */
    public boolean hasPermission(String permissionCode) {
        LoginUser userInfo = loginTool.getUserInfo();
        boolean b = permissionService.hasPermission(permissionCode, userInfo);
        return b;
    }

}
