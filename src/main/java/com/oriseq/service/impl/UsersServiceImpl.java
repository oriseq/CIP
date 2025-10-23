package com.oriseq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.oriseq.common.utils.RegexUtils;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.controller.utils.PasswordEncoderUtils;
import com.oriseq.dtm.dto.UserDTO;
import com.oriseq.dtm.entity.OpenPermission;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.dtm.vo.LoginUserVO;
import com.oriseq.dtm.vo.user.AddUserVO;
import com.oriseq.mapper.UsersMapper;
import com.oriseq.service.IOpenPermissionService;
import com.oriseq.service.IUserGroupService;
import com.oriseq.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Hacah
 * @className: StudentServiceImpl
 * @description: 用户
 * @date 2024/4/23 15:13
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, User> implements IUsersService {

    @Autowired
    private IUserGroupService userGroupService;
    @Autowired
    private IOpenPermissionService openPermissionService;


    @Override
    public User selectUserByName(String username) {
        User user = baseMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        return user;
    }

    @Override
    public Boolean verifyToken(String token) throws Exception {
        String usernameFromToken = JwtUtils.getUsernameFromToken(token);
        // 是否存在用户
        if (Objects.isNull(usernameFromToken) || "".equals(usernameFromToken)) {
            log.debug("用户为空值或null");
            return false;
        }
        User user = this.selectUserByName(usernameFromToken);
        if (Objects.nonNull(user)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean existingPhone(String phoneNum) {
        LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getPhoneNumber, phoneNum);
        Long l = baseMapper.selectCount(eq);
        if (l > 0) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserAndCount(Long userGroupId, User user) {
        // 3. 更新用户组的员工数量
        UserGroup byId = userGroupService.getById(userGroupId);
        // 查询最新最准确usersService的用户数量
        long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUserGroupId, userGroupId));
        byId.setAmountOfUsers(count + 1);
        userGroupService.updateById(byId);
        // 4. 存储用户
        baseMapper.insert(user);
    }

    @Override
    public int updateLoginTime(String phone) {
        // 更新登录时间，通过手机号
        User user = new User();
        user.setLastLoginTime(LocalDateTime.now());
        LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getPhoneNumber, phone);
        int update = baseMapper.update(user, eq);
        return update;
    }

    @Override
    public Boolean existingMail(String mail) {
        LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getMail, mail);
        Long l = baseMapper.selectCount(eq);
        if (l > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updatePassword(String phoneOrMail, String newPassword) {
        boolean isPhone = false;
        // 1.判断手机号正常
        isPhone = RegexUtils.isPhoneNumber(phoneOrMail);
        if (!isPhone && !RegexUtils.isEmail(phoneOrMail)) {
            throw new RuntimeException("手机号或邮箱格式错误");
        }

        // 确认库中是否存在手机号或密码，修改库中的密码
        if (isPhone) {
            String phone = phoneOrMail;
            Boolean exist = this.existingPhone(phone);
            if (!exist) {
                throw new RuntimeException("手机号不存在");
            }
            // 修改库中的密码
            User user = new User();
            user.setPassword(newPassword);
            LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getPhoneNumber, phone);
            baseMapper.update(user, eq);
        } else {
            String mail = phoneOrMail;
            Boolean exist = this.existingMail(mail);
            if (!exist) {
                throw new RuntimeException("邮箱不存在");
            }
            // 修改库中的密码
            User user = new User();
            user.setPassword(newPassword);
            LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getMail, mail);
            baseMapper.update(user, eq);
        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginUserVO getUserInfo(String phone) {
        LambdaQueryWrapper<User> eq = new QueryWrapper<User>().lambda().eq(User::getPhoneNumber, phone);
        User user = this.baseMapper.selectOne(eq);
        // 查出用户组信息
        UserGroup userGroup = userGroupService.getById(user.getUserGroupId());

        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUserId(user.getId().toString());
        loginUserVO.setUsername(user.getUsername());
        loginUserVO.setAvatar(null);
        loginUserVO.setPhone(user.getPhoneNumber());
        loginUserVO.setIsSuper(user.getIsSuper() == 1 ? true : false);
        loginUserVO.setIsGroupSuper(user.getIsGroupSuper() == 1 ? true : false);
//        loginUserVO.setHomePath("/dashboard/analysis");
        String groupName = userGroup.getGroupName();
        loginUserVO.setUserGroupName(groupName);
        loginUserVO.setIsInternalGroup(userGroup.getIsInternalGroup());
        loginUserVO.setRealName(user.getRealName());
        loginUserVO.setEmail(user.getMail());
        LoginUserVO.Role role = new LoginUserVO.Role();
        role.setRoleName(groupName);
        role.setValue(groupName);
        List<LoginUserVO.Role> strings = Collections.singletonList(role);
        loginUserVO.setRoles(strings);


        return loginUserVO;
    }

    @Override
    public List<UserDTO> userInfoList(LambdaQueryWrapper<User> userLambdaQueryWrapper) {
        List<UserDTO> userDTOS = baseMapper.selectUserInfo(userLambdaQueryWrapper);
        return userDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(AddUserVO addUserVO) {
        User userOrigin = baseMapper.selectById(addUserVO.getId());
        User user = new User();
        user.setId(addUserVO.getId());
        user.setMail(addUserVO.getMail());
        // 排除和用户手机号一致的情况判断手机号是否存在
        if (!Objects.equals(userOrigin.getPhoneNumber(), addUserVO.getPhoneNumber()) && addUserVO.getPhoneNumber() != null) {
            Boolean exist = this.existingPhone(addUserVO.getPhoneNumber());
            if (exist) {
                throw new RuntimeException("手机号已存在");
            }
        }
        user.setPhoneNumber(addUserVO.getPhoneNumber());
        user.setUsername(addUserVO.getUsername());
        user.setRealName(addUserVO.getRealName());
        Optional.ofNullable(addUserVO.getAvail()).ifPresent(aBoolean -> user.setAvail(aBoolean));
        Optional.ofNullable(addUserVO.getIsSuper()).ifPresent(a -> {
            user.setIsSuper(a);
            // 如果是变成了超级管理员，那么删掉该用户所有权限中间表的数据
            if (a == 1) {
                openPermissionService.remove(new LambdaQueryWrapper<OpenPermission>().eq(OpenPermission::getUserId, addUserVO.getId()));
            }
        });
        Optional.ofNullable(addUserVO.getIsGroupSuper()).ifPresent(a -> user.setIsGroupSuper(a));
        Optional.ofNullable(addUserVO.getUserGroupId()).ifPresent(a -> user.setUserGroupId(a));
        baseMapper.updateById(user);
    }

    @Override
    public List<User> getInnerGroupUserList() {
        MPJLambdaWrapper<User> eq = new MPJLambdaWrapper<User>()
                .selectAll(User.class)
                .leftJoin(UserGroup.class, UserGroup::getId, User::getUserGroupId)
                .eq(UserGroup::getIsInternalGroup, 1);
        List<User> users = baseMapper.selectJoinList(eq);
        return users;
    }

    @Override
    public boolean changePassword(Long userId, String newPassword) {
        User user = new User();
        user.setId(Long.valueOf(userId));
        // 加密
        user.setPassword(PasswordEncoderUtils.encodePassword(newPassword));
        baseMapper.updateById(user);
        return true;
    }

    @Override
    public boolean checkPassword(Long userId, String oldPassword) {
        User one = baseMapper.selectById(userId);
        if (PasswordEncoderUtils.matches(oldPassword, one.getPassword())) {
            return true;
        } else {
            return false;
        }
    }


}
