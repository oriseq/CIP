package com.oriseq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.config.LoginTool;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Permission;
import com.oriseq.dtm.entity.UserGroup;

import java.util.List;

/**
 * @author Hacah
 * @className: IUserGroup
 * @date 2024/5/6 15:48
 */
public interface IUserGroupService extends IService<UserGroup> {
    /**
     * 获取用户菜单权限
     *
     * @param user
     * @return
     */
    List<Permission> getMenuPermissionByPhone(LoginUser user);

    /**
     * 获取用户按钮权限
     *
     * @param userId
     * @return
     */
    List<Permission> getButtonPermission(Long userId);

    /**
     * 用户拥有的权限id
     *
     * @param userId
     * @return
     */
    List<Permission> getPermissionIds(Long userId);

    void deleteUserInfoCacheOfGroup(LoginTool loginTool, Long id);
}
