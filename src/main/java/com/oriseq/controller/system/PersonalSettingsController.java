package com.oriseq.controller.system;

import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.vo.personalSettings.ChangeBasicInformationVO;
import com.oriseq.dtm.vo.personalSettings.ChangePasswordVO;
import com.oriseq.service.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2025/2/27 15:40
 */

@RestController
@RequestMapping("/personalSettings")
@Slf4j
@EnableLogging
public class PersonalSettingsController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private LoginTool loginTool;


    /**
     * 修改用户基本信息
     *
     * @param changeBasicInformationVO 包含用户新信息的请求体
     * @return 操作结果
     */
    @PostMapping("basicInformation")
    public Result<Object> changeBasicInformation(@Validated @RequestBody ChangeBasicInformationVO changeBasicInformationVO) {
        /*
            {
              "id": 123,
              "phoneNumber": "13800138000",
              "mail": "user@example.com",
              "username": "user_name_123",
              "realName": "张三"
            }
        * */
        // 1.判断手机号是否存在 并 是否与本身用户的手机号一致
        LoginUser userInfo = loginTool.getUserInfo();
        // 要修改的手机号与当前用户手机号一致，则不进行判断
        boolean equals = Objects.equals(userInfo.getPhone(), changeBasicInformationVO.getPhoneNumber());
        Boolean b = usersService.existingPhone(changeBasicInformationVO.getPhoneNumber());
        if (!equals && b) {
            return Result.defaultErrorByMessage("手机号已存在");
        }
        // 2.更新用户信息
        User user = new User();
        // 当前用户修改自己本身
        user.setId(Long.valueOf(userInfo.getUserId()));
        user.setPhoneNumber(changeBasicInformationVO.getPhoneNumber());
        user.setMail(changeBasicInformationVO.getMail());
        user.setUsername(changeBasicInformationVO.getUsername());
        user.setRealName(changeBasicInformationVO.getRealName());
        usersService.updateById(user);
        return Result.defaultSuccessByMessage("修改成功");
    }


    /**
     * 处理密码变更请求
     *
     * @param changePasswordVO 包含用户提交的新密码和确认密码的实体对象
     * @return 返回密码变更的结果
     */
    @PostMapping("password")
    public Result<Object> changePassword(@Validated @RequestBody ChangePasswordVO changePasswordVO) {
        // 1.得到当前用户id
        LoginUser userInfo = loginTool.getUserInfo();
        String userIdStr = userInfo.getUserId();
        Long userId = Long.valueOf(userIdStr);
        // 判断旧密码和新密码是否一致
        if (changePasswordVO.getNewPassword().equals(changePasswordVO.getOldPassword())) {
            return Result.defaultErrorByMessage("新密码不能与旧密码相同");
        }
        // 检查旧密码是否正确
        if (!usersService.checkPassword(userId, changePasswordVO.getOldPassword())) {
            return Result.defaultErrorByMessage("旧密码不正确");
        }
        // 检查两次输入的密码是否一致
        if (!changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
            return Result.defaultErrorByMessage("两次密码不一致");
        }
        // 尝试更新用户密码
        if (usersService.changePassword(userId, changePasswordVO.getNewPassword())) {
            return Result.defaultSuccessByMessage("修改成功");
        } else {
            return Result.defaultErrorByMessage("修改失败");
        }
    }

}
