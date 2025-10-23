package com.oriseq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.config.LoginTool;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Permission;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.mapper.PermissionMapper;
import com.oriseq.mapper.UserGroupMapper;
import com.oriseq.mapper.UsersMapper;
import com.oriseq.service.IUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hacah
 * @className: UserGroupServiceImpl
 * @date 2024/5/6 15:49
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements IUserGroupService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public List<Permission> getMenuPermissionByPhone(LoginUser user) {
        // 判断是否超级管理员
        if (user.getIsSuper() == 1) {
            // 全部菜单
            // (p.type = "M" or p.type = "C")
            LambdaQueryWrapper<Permission> ew = new LambdaQueryWrapper<Permission>().eq(Permission::getType, "M")
                    .or().eq(Permission::getType, "C");
            List<Permission> permissionByPhone = permissionMapper.selectList(ew);
            return permissionByPhone;
        }
        // 其他用户
        List<Permission> permissionByPhone = baseMapper.getGeneralUserPermissionByPhone(user.getPhone());
        // 去重
        List<Permission> collect = permissionByPhone.stream().collect(Collectors.toSet()).stream().toList();
        return collect;
    }

    @Override
    public List<Permission> getButtonPermission(Long userId) {
        User user = usersMapper.selectById(userId);
        LambdaQueryWrapper<Permission> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(Permission::getLevel, 2);
        List<Permission> permissions = permissionMapper.selectList(userLambdaQueryWrapper);
        return permissions;
    }

    @Override
    public List<Permission> getPermissionIds(Long userId) {
        User user = usersMapper.selectById(userId);
        LambdaQueryWrapper<User> ew = new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, user.getPhoneNumber());
        List<Permission> permissionByPhone = baseMapper.getButtonPermission(ew);
        return permissionByPhone;
    }

    @Override
    public void deleteUserInfoCacheOfGroup(LoginTool loginTool, Long id) {
        List<User> users = usersMapper.selectList(new LambdaQueryWrapper<User>().in(User::getUserGroupId, id));
        users.stream().forEach(user -> {
            // 删除缓存
            loginTool.deleteUserInfoCache(user.getPhoneNumber());
        });
    }
}
