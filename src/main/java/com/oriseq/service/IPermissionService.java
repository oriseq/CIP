package com.oriseq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Permission;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:36
 */
public interface IPermissionService extends IService<Permission> {
    boolean hasPermission(String permission, LoginUser userInfo);
}
