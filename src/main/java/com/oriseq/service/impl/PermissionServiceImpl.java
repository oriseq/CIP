package com.oriseq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Permission;
import com.oriseq.mapper.PermissionMapper;
import com.oriseq.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/7 9:31
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Override
    public boolean hasPermission(String permission, LoginUser userInfo) {
        //
        if (userInfo.isSuper()) {
            return true;
        }
        Integer i = baseMapper.hasPermission(permission, userInfo.getUserId());
        if (i != null && i > 0) {
            return true;
        }
        return false;
    }
}
