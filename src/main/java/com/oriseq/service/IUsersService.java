package com.oriseq.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.UserDTO;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.vo.LoginUserVO;
import com.oriseq.dtm.vo.user.AddUserVO;

import java.util.List;

/**
 * @author Hacah
 * @className: UsersService
 * @description: 用户
 * @date 2024/4/23 15:12
 */
public interface IUsersService extends IService<User> {
    User selectUserByName(String username);

    Boolean verifyToken(String token) throws Exception;

    Boolean existingPhone(String phoneNum);

    /**
     * 更新用户数量和存储用户
     *
     * @param userGroupId
     * @param user
     */
    void saveUserAndCount(Long userGroupId, User user);

    /**
     * 通过手机号更新用户登录时间
     *
     * @param phone
     * @return
     */
    int updateLoginTime(String phone);

    /**
     * 判断邮箱是否已存在
     *
     * @param mail
     * @return
     */
    Boolean existingMail(String mail);

    /**
     * 更新密码 通过手机号或者邮箱
     *
     * @param phoneOrMail
     * @param newPassword
     */
    void updatePassword(String phoneOrMail, String newPassword);

    /**
     * 获取用户信息,包含角色信息
     *
     * @param phone
     * @return
     */
    LoginUserVO getUserInfo(String phone);

    /**
     * 用户信息
     *
     * @param userLambdaQueryWrapper
     * @return
     */
    List<UserDTO> userInfoList(LambdaQueryWrapper<User> userLambdaQueryWrapper);


    /**
     * 更新用户信息
     *
     * @param addUserVO
     */
    void updateUserInfo(AddUserVO addUserVO);

    /**
     * 获取内部组用户列表
     *
     * @return
     */
    List<User> getInnerGroupUserList();

    /**
     * 修改用户密码
     *
     * @param userId      用户ID，用于标识需要修改密码的用户
     * @param newPassword 新密码，用户希望设置的新密码
     * @return 返回密码修改操作是否成功
     */
    boolean changePassword(Long userId, String newPassword);

    /**
     * 检查用户密码是否正确
     *
     * @param userId      用户ID，用于标识需要检查密码的用户
     * @param oldPassword 旧密码，用户输入的旧密码
     * @return 返回密码检查操作是否成功
     */
    boolean checkPassword(Long userId, String oldPassword);
}
