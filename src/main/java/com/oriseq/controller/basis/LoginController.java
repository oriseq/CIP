package com.oriseq.controller.basis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oriseq.common.mail.MailTool;
import com.oriseq.common.sms.SMSTemplateCode;
import com.oriseq.common.sms.SampleSmsTool;
import com.oriseq.common.utils.RandomCodeGenerator;
import com.oriseq.common.utils.RegexUtils;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.controller.utils.PasswordEncoderUtils;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.SysConfig;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.dtm.vo.LoginUserVO;
import com.oriseq.dtm.vo.ResetPasswordVO;
import com.oriseq.dtm.vo.SmsLoginVO;
import com.oriseq.dtm.vo.UserVO;
import com.oriseq.service.IUserGroupService;
import com.oriseq.service.IUsersService;
import com.oriseq.service.SysConfigService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Hacah
 * @className: LoginController
 * @description: 登录注册
 * @date 2024/4/23 14:07
 */
@RestController
@Slf4j
public class LoginController {
    @Autowired
    private IUsersService usersService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SampleSmsTool sampleSmsTool;

    @Autowired
    private MailTool mailTool;

    @Autowired
    private SMSTemplateCode templateCode;

    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private LoginTool loginTool;

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 验证码过期时间  min
     */
    private Integer codeTimeout = 5;
    /**
     * 经过过久后，允许重新发送验证码时间  min
     */
    private Integer reSentCodeTime = 1;

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

    /**
     * @param userVO 登录参数
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("/login")
    public Result<LoginUserVO> login(@RequestBody UserVO userVO) {
        // TODO 请求限制
        // 1.检查用户是否存在
        // 用户名 或 手机号 或 邮箱
        String account = userVO.getAccount();
        String password = userVO.getPassword();
        // 是否空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.defaultErrorByMessage("账号或密码不能空");
        }

        // 2.判断是用户名 或 手机号 或 邮箱
        LambdaQueryWrapper<User> eq = null;
        if (RegexUtils.isPhoneNumber(account)) {
            eq = new QueryWrapper<User>().lambda().eq(User::getPhoneNumber, account);
        } else if (RegexUtils.isEmail(account)) {
            eq = new QueryWrapper<User>().lambda().eq(User::getMail, account);
        } else {
            eq = new QueryWrapper<User>().lambda().eq(User::getUsername, account);
        }
        User one = usersService.getOne(eq);

        // 2.1存在，返回token
        if (Objects.nonNull(one)) {
            // 3.判断密码
            if (!PasswordEncoderUtils.matches(password, one.getPassword())) {
                return Result.defaultErrorByMessage("账号或密码错误");
            }
            String token = JwtUtils.generateToken(one.getPhoneNumber());
            // 查询用户组
            UserGroup byId = userGroupService.getById(one.getUserGroupId());
            LoginUserVO loginUserVO = new LoginUserVO();
            loginUserVO.setToken(token);
            loginUserVO.setUserId(one.getId().toString());
            loginUserVO.setUsername(one.getUsername());
            loginUserVO.setPhone(one.getPhoneNumber());
            loginUserVO.setUserGroupName(Optional.ofNullable(byId).map(UserGroup::getGroupName).orElse(""));
            // 更新登录时间
            one.setLastLoginTime(LocalDateTime.now());
            usersService.updateById(one);
            // 缓存用户信息
//            loginTool.setUserInfo(token);
            return Result.defaultSuccessByMessageAndData("登录成功", loginUserVO);
        } else {
            // 2.2不存在
            log.debug("不存在该用户");
            return Result.defaultErrorByMessage("账号或密码错误");
        }
    }


    /**
     * 获取验证码
     *
     * @param phoneNum
     * @return {@link Result}<{@link String}>
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/smsVerificationCode")
    public Result<String> smsVerificationCode(@RequestParam("phoneNum") String phoneNum) throws ExecutionException, InterruptedException {
        //TODO 请求限制

        // 1.判断短信配置是否开启
        SysConfig config = sysConfigService.getByName("login.sms.enabled");
        if (config == null || !"true".equals(config.getConfigValue().toString())) {
            return Result.defaultErrorByMessage("系统未开启短信功能");
        }
        // 1.判断手机号正常
        if (!RegexUtils.isPhoneNumber(phoneNum)) {
            throw new RuntimeException("手机号格式错误");
        }
        // 存在reids上验证码就不再次发送短信
        String vc = stringRedisTemplate.opsForValue().get(phoneNum);
        // 获取过期时间
        if (StringUtils.isNotBlank(vc)) {
            long expireSecond = stringRedisTemplate.getExpire(phoneNum);
            // 过去的时间
            long postTime = codeTimeout * 60 - expireSecond;
            long leveTime = reSentCodeTime * 60 - postTime;
            if (leveTime >= 0) {
                return Result.defaultErrorByMessage("验证码已发送，请" + leveTime + "秒后再试");
            }
        }

        // 1.1在数据库
        Boolean exist = usersService.existingPhone(phoneNum);
        // 2.生成验证码，保存，发送
        if (exist) {
            String s = RandomCodeGenerator.generateRandomCode(4);
            // 保存 redis
            stringRedisTemplate.opsForValue().set(phoneNum, s, codeTimeout, TimeUnit.MINUTES);
            // 调用短信服务，发送短信
            sampleSmsTool.sendSmsForLogin(s, phoneNum, templateCode.getTemplateCode().get("login"));
            return Result.defaultSuccessByMessage("验证码发送成功");
        }
        return Result.defaultErrorByMessage("手机号不存在");
    }

    /**
     * 获取验证码：用于重置密码
     *
     * @param phoneOrMail
     * @return {@link Result}<{@link String}>
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/verificationCode")
    public Result<String> verificationCode(@RequestParam("phoneOrMail") String phoneOrMail) throws ExecutionException, InterruptedException {
        //TODO 请求限制

        boolean isPhone = false;
        // 1.判断手机号正常
        isPhone = RegexUtils.isPhoneNumber(phoneOrMail);
        if (!isPhone && !RegexUtils.isEmail(phoneOrMail)) {
            throw new RuntimeException("手机号或邮箱格式错误");
        }

        // 存在reids上验证码就不再次发送短信
        String vc = stringRedisTemplate.opsForValue().get(phoneOrMail);
        // 获取过期时间,没过期就返回
        if (StringUtils.isNotBlank(vc)) {
            long expireSecond = stringRedisTemplate.getExpire(phoneOrMail);
            // 过去的时间
            // 过去的时间
            long postTime = codeTimeout * 60 - expireSecond;
            long leveTime = reSentCodeTime * 60 - postTime;
            if (leveTime >= 0) {
                return Result.defaultErrorByMessage("验证码已发送，请" + leveTime + "秒后再试");
            }
        }

        if (isPhone) {
            // 1.1在数据库
            Boolean exist = usersService.existingPhone(phoneOrMail);
            // 2.生成验证码，保存，发送
            if (exist) {
                String s = RandomCodeGenerator.generateRandomCode(4);
                // 保存 redis
                stringRedisTemplate.opsForValue().set(phoneOrMail, s, codeTimeout, TimeUnit.MINUTES);
                // 调用短信服务，发送短信
                sampleSmsTool.sendSmsForLogin(s, phoneOrMail, templateCode.getTemplateCode().get("reset_password"));
                return Result.defaultSuccessByMessage("验证码发送成功");
            }
            return Result.defaultErrorByMessage("手机号不存在");
        } else {
            String mail = phoneOrMail;
            // 1.1在数据库
            Boolean exist = usersService.existingMail(mail);
            // 2.生成验证码，保存，发送
            if (exist) {
                String s = RandomCodeGenerator.generateRandomCode(4);
                // 保存 redis
                stringRedisTemplate.opsForValue().set(mail, s, codeTimeout, TimeUnit.MINUTES);
                // 调用邮箱服务，发送短信
                mailTool.sendReSetPasswordMail(mail, s);
                return Result.defaultSuccessByMessage("验证码发送成功");
            }
            return Result.defaultErrorByMessage("邮箱不存在");
        }
    }

    /**
     * 短信登录
     *
     * @param smsLoginVo
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("/smsLogin")
    public Result<LoginUserVO> smsLogin(@RequestBody SmsLoginVO smsLoginVo) {
        //TODO 请求限制

        // 1.判断短信配置是否开启
        SysConfig config = sysConfigService.getByName("login.sms.enabled");
        if (config == null || !"true".equals(config.getConfigValue().toString())) {
            return Result.defaultErrorByMessage("系统未开启短信功能");
        }
        // 2.判断验证码正确
        String vc = stringRedisTemplate.opsForValue().get(smsLoginVo.getPhone());
        if (StringUtils.isNotBlank(vc) && vc.equals(smsLoginVo.getVerificationCode())) {
            // 1.判断手机号正常且在数据库
            Boolean exist = usersService.existingPhone(smsLoginVo.getPhone());
            if (exist) {
                // 登录
                String token = JwtUtils.generateToken(smsLoginVo.getPhone());
                LoginUserVO userInfo = usersService.getUserInfo(smsLoginVo.getPhone());
                userInfo.setToken(token);
                // 更新登录时间
                usersService.updateLoginTime(smsLoginVo.getPhone());
                // 缓存用户信息
//                loginTool.setUserInfo(token);
                return Result.defaultSuccessByMessageAndData("登录成功", userInfo);
            } else {
                return Result.defaultErrorByMessage("手机号不存在");
            }
        }
        return Result.defaultErrorByMessage("验证码错误");
    }


    @PostMapping("/resetPasswd")
    public Result<Object> resetPasswd(@Validated @RequestBody ResetPasswordVO resetPasswdVO, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> collect = fieldErrors.stream().map(e -> e.getField() + ":" + e.getDefaultMessage()).collect(Collectors.toList());
        if (collect.size() > 0) {
            return Result.defaultErrorByMessage(collect.toString());
        }

        /*
            1.确认redis中匹配验证码
            2.确认库中是否存在手机号或密码，修改库中的密码
        */
        // 1.确认redis中匹配验证码
        String vc = stringRedisTemplate.opsForValue().get(resetPasswdVO.getPhoneOrMail());
        if (StringUtils.isNotBlank(vc) && vc.equals(resetPasswdVO.getVerificationCode())) {
            // 删除验证码 避免再次使用
            stringRedisTemplate.delete(resetPasswdVO.getPhoneOrMail());
            //  2.确认库中是否存在手机号或密码，修改库中的密码
            // 加密
            String encodePassword = PasswordEncoderUtils.encodePassword(resetPasswdVO.getNewPassword());
            usersService.updatePassword(resetPasswdVO.getPhoneOrMail(), encodePassword);
            return Result.defaultSuccessByMessage("密码修改成功");
        }
        return Result.defaultErrorByMessageAndData("验证码错误", null);
    }


    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping("logout")
    public Result<Object> logout(HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 清除缓存
        stringRedisTemplate.delete(userInfo.getPhone());
        return Result.defaultSuccessByMessage("退出成功");
    }

}