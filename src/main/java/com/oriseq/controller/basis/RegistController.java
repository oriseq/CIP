package com.oriseq.controller.basis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oriseq.controller.utils.PasswordEncoderUtils;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.entity.InvitationCode;
import com.oriseq.dtm.entity.SysConfig;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.vo.RegistUserVO;
import com.oriseq.service.IInvitationCodeService;
import com.oriseq.service.IUsersService;
import com.oriseq.service.SysConfigService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hacah
 * @className: RegistController
 * @description: 注册
 * @date 2024/4/30 13:47
 */
@RestController
@Slf4j
public class RegistController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IInvitationCodeService invitationCodeService;

    @Autowired
    private SysConfigService sysConfigService;


    @PostMapping("/regist")
    public Result<String> regist(@RequestBody @Validated RegistUserVO registUserVO, BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage())
                .collect(Collectors.toList());
        if (collect.size() > 0) {
            return Result.defaultErrorByMessage(collect.toString());
        }
        // 查询系统配置，确认是否开启注册
        SysConfig config = sysConfigService.getByName("login.register.allowRegister");
        if (config == null || !"true".equals(config.getConfigValue())) {
            return Result.defaultErrorByMessage("系统未开启注册");
        }
        /* 注册逻辑
        1. 检查是否存在手机或用户或邮箱
        2. 通过邀请码确认是属于什么用户组，查询邀请码表并得到用户组id
        3. 更新用户组的员工数量
        4. 存储用户
         */
        // 1. 检查是否存在手机或用户或邮箱
        List<User> list = usersService.list(new QueryWrapper<User>().lambda().eq(User::getPhoneNumber, registUserVO.getPhone())
                .or().eq(User::getMail, registUserVO.getEmail()).or().eq(User::getUsername, registUserVO.getUsername()));
        // 判断哪个重复
        if (list.size() > 0) {
            ArrayList<String> infoList = new ArrayList<>();
            for (User user : list) {
                if (StringUtils.isNotBlank(user.getUsername()) && user.getUsername().equals(registUserVO.getUsername())) {
                    infoList.add("用户名");
                }
                if (StringUtils.isNotBlank(user.getPhoneNumber()) && user.getPhoneNumber().equals(registUserVO.getPhone())) {
                    infoList.add("手机号");
                }
                if (StringUtils.isNotBlank(user.getUsername()) && user.getMail().equals(registUserVO.getEmail())) {
                    infoList.add("邮箱");
                }
            }
            return Result.defaultErrorByMessageAndData(infoList.toString() + "已存在", infoList.toString());
        }

        // 2. 通过邀请码确认是属于什么用户组，查询邀请码表并得到用户组id
        String inviteCode = registUserVO.getInviteCode();
        InvitationCode one = invitationCodeService.getOne(new QueryWrapper<InvitationCode>().lambda().eq(InvitationCode::getInvitationCode, inviteCode));
        if (one == null) {
            return Result.defaultErrorByMessage("邀请码不存在");
        } else if (Objects.nonNull(one.getExpiryDate())) {
            // 检查邀请码过期时间
            if (one.getExpiryDate().isBefore(LocalDateTime.now())) {
                return Result.defaultErrorByMessage("邀请码已过期");
            }
        }

        Long userGroupId = one.getUserGroupId();

        // 3. 更新用户组的员工数量
        // 4. 存储用户
        User user = new User();
        user.setMail(registUserVO.getEmail());
        // 加密
        user.setPassword(PasswordEncoderUtils.encodePassword(registUserVO.getPassword()));
        user.setPhoneNumber(registUserVO.getPhone());
        user.setUsername(registUserVO.getUsername());
        user.setUserGroupId(userGroupId);
        user.setCreationTime(LocalDateTime.now());
        user.setRealName(registUserVO.getRealName());
        usersService.saveUserAndCount(userGroupId, user);
        String string = registUserVO.toString();
        log.info(string);
        return Result.defaultSuccessByMessage("注册成功");
    }


//    @RequestMapping("test")
//    @RateLimit(limit = 2, duration = 60)
//    public Result test() {
//        return Result.defaultSuccessByMessage("test");
//    }

}
