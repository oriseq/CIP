package com.oriseq.controller.system;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.common.utils.RandomCodeGenerator;
import com.oriseq.config.LoginTool;
import com.oriseq.config.PermissionTool;
import com.oriseq.config.permission.RequiredPermission;
import com.oriseq.controller.utils.PasswordEncoderUtils;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.UserDTO;
import com.oriseq.dtm.entity.InvitationCode;
import com.oriseq.dtm.entity.MsgGroup;
import com.oriseq.dtm.entity.OpenPermission;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.vo.MsgUpdateGroupVO;
import com.oriseq.dtm.vo.user.AddUserVO;
import com.oriseq.dtm.vo.user.PermissionUserVO;
import com.oriseq.service.IInvitationCodeService;
import com.oriseq.service.IOpenPermissionService;
import com.oriseq.service.IUsersService;
import com.oriseq.service.MsgGroupService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/31 14:07
 */
@RestController
@RequestMapping("system/user")
@Slf4j
@EnableLogging
public class UserController {


    @Autowired
    private IUsersService usersService;
    @Autowired
    private IInvitationCodeService invitationCodeService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private IOpenPermissionService openPermissionService;
    @Autowired
    private PermissionTool permissionTool;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MsgGroupService msgGroupService;

    private String prefix = "invitationCode:";

    @GetMapping("list")
    @RequiredPermission("system:user:query")
    public Result<List<UserDTO>> allUser(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "realName", required = false) String realName
            , HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.like(StringUtils.isNotBlank(username), User::getUsername, username);
        userLambdaQueryWrapper.like(StringUtils.isNotBlank(phoneNumber), User::getPhoneNumber, phoneNumber);
        userLambdaQueryWrapper.like(StringUtils.isNotBlank(mail), User::getMail, mail);
        userLambdaQueryWrapper.like(StringUtils.isNotBlank(realName), User::getRealName, realName);
        List<UserDTO> userInfoList = usersService.userInfoList(userLambdaQueryWrapper);
        // 如果是超级管理员，获取邀请码,设置邀请码属性
        if (loginTool.isSuper(userInfo)) {
            // 缓存中获取
            userInfoList = userInfoList.stream().map(userDTO -> {
                String phoneNumber1 = userDTO.getPhoneNumber();
                String codeCache = stringRedisTemplate.opsForValue().get(prefix + phoneNumber1);
                userDTO.setInvitationCode(codeCache);
                return userDTO;
            }).toList();
        }

        // 不同权限返回
        if (loginTool.isSuper(userInfo)) {
            return Result.defaultSuccessByMessageAndData("查询成功", userInfoList);
        } else {
            // 其他用户，判断权限过滤
            userInfoList = userInfoList.stream().filter(userDTO -> userInfo.getUserGroupId().equals(userDTO.getUserGroupId())).collect(Collectors.toList());
            return Result.defaultSuccessByMessageAndData("查询成功", userInfoList);
        }


    }


    /**
     * 获取邀请码
     *
     * @param request
     * @return
     */
    @RequiredPermission("system:user:invitationCode")
    @GetMapping("invitationCode")
    public Result<Map> getInvitationCode(HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 1缓存获取
        String codeCache = stringRedisTemplate.opsForValue().get(prefix + userInfo.getPhone());
        Long expire = stringRedisTemplate.getExpire(prefix + userInfo.getPhone());
        LocalDateTime exT = LocalDateTime.now().plusSeconds(expire);
        if (StrUtil.isNotBlank(codeCache)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code", codeCache);
            jsonObject.addProperty("expireTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(exT));
            Map map = new Gson().fromJson(jsonObject, Map.class);
            return Result.defaultSuccessByMessageAndData("查询成功", map);
        }
        // 数据库获取并缓存，若不存在
        String code = RandomCodeGenerator.generateRandomCodeLong();
        // 2缓存
        stringRedisTemplate.opsForValue().set(prefix + userInfo.getPhone(), code, 1, TimeUnit.DAYS);
        // 3数据库保存
        LocalDateTime now = LocalDateTime.now();
        // 1天后过期
        LocalDateTime expTime = LocalDateTime.now().plusDays(1);
        InvitationCode invitationCode = new InvitationCode(userInfo.getUserGroupId(), code, expTime, now);
        invitationCodeService.save(invitationCode);
        // 返回
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("expireTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(expTime));
        Map map = new Gson().fromJson(jsonObject, Map.class);
        return Result.defaultSuccessByMessageAndData("查询成功", map);
    }

    /**
     * 新增/更新用户
     * <p>
     * 涉及权限情况，编写逻辑需要更细致
     *
     * @return
     */
    @PostMapping("user")
    public Result<ArrayList<String>> addOrUpdateUser(@RequestBody @Validated AddUserVO addUserVO, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // isSupe权限判定
        if (!loginTool.isSuper(userInfo) && addUserVO.getIsSuper() == 1) {
            return Result.defaultErrorByMessage("无权限操作");
        }
        // 修改不用用户组用户 userGroupId权限判定
        if (!loginTool.isSuper(userInfo) && addUserVO.getUserGroupId() != null
                && !addUserVO.getUserGroupId().equals(userInfo.getUserGroupId())) {
            return Result.defaultErrorByMessage("无权限操作");
        }
        // 是否更新
        if (addUserVO.getId() != null) {
            if (!permissionTool.hasPermission("system:user:update")) {
                return Result.defaultErrorByMessage("无权限操作");
            }
            // 权限细致控制，非超级管理员情况下：只允许修改同组的用户
            if (!loginTool.isSuper(userInfo) && !userInfo.getUserGroupId().equals(addUserVO.getUserGroupId())) {
                return Result.defaultErrorByMessage("无权限操作");
            }
            usersService.updateUserInfo(addUserVO);
            // 删除缓存
            loginTool.deleteUserInfoCache(addUserVO.getPhoneNumber());
            return Result.defaultSuccessByMessageAndData("更新成功", null);
        }
        // 新增逻辑
        if (!permissionTool.hasPermission("system:user:add")) {
            return Result.defaultErrorByMessage("无权限操作");
        }
        // 1. 检查是否存在手机或用户或邮箱
        List<User> list = usersService.list(new QueryWrapper<User>().lambda()
                .eq(User::getPhoneNumber, addUserVO.getPhoneNumber())
                .or().eq(User::getMail, addUserVO.getMail()).or().eq(User::getUsername, addUserVO.getUsername()));
        // 判断哪个重复
        if (list.size() > 0) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (User user : list) {
                if (StringUtils.isNotBlank(user.getUsername()) && user.getUsername().equals(addUserVO.getUsername())) {
                    arrayList.add("用户名已存在");
                }
                if (StringUtils.isNotBlank(user.getPhoneNumber()) && user.getPhoneNumber().equals(addUserVO.getPhoneNumber())) {
                    arrayList.add("手机号已存在");
                }
                if (StringUtils.isNotBlank(user.getUsername()) && user.getMail().equals(addUserVO.getMail())) {
                    arrayList.add("邮箱已存在");
                }
            }
            return Result.defaultErrorByMessageAndData(arrayList.toString(), arrayList);
        }

        // 2. 得到用户组id
        Long userGroupId = userInfo.getUserGroupId();
        // 超级管理员按照参数决定
        if (loginTool.isSuper(userInfo)) {
            userGroupId = addUserVO.getUserGroupId();
            if (userGroupId == null) {
                return Result.defaultErrorByMessage("参数错误, 请选择用户组");
            }
        }
        // 3. 更新用户组的员工数量
        // 4. 存储用户
        User user = new User();
        user.setMail(addUserVO.getMail());
        // 加密
        user.setPassword(PasswordEncoderUtils.encodePassword(addUserVO.getPassword()));
        user.setPhoneNumber(addUserVO.getPhoneNumber());
        user.setUsername(addUserVO.getUsername());
        user.setUserGroupId(userGroupId);
        user.setIsSuper(addUserVO.getIsSuper());
        user.setIsGroupSuper(addUserVO.getIsGroupSuper());
        user.setCreationTime(LocalDateTime.now());
        user.setRealName(addUserVO.getRealName());
        usersService.saveUserAndCount(userGroupId, user);
        return Result.defaultSuccessByMessage("添加成功");
    }

    /**
     * 删除用户
     *
     * @return
     */
    @RequiredPermission("system:user:delete")
    @DeleteMapping("user")
    public Result<ArrayList<String>> deleteUser(@RequestBody List<String> ids, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 判断是否是超级管理员
        if (loginTool.isSuper(userInfo)) {
            usersService.removeBatchByIds(ids);
            return Result.defaultSuccessByMessage("操作成功");
        } else {
            List<User> users = usersService.listByIds(ids);
            long count = users.stream().filter(user -> !user.getUserGroupId().equals(userInfo.getUserGroupId())).count();
            if (count > 0) {
                return Result.defaultErrorByMessage("操作失败，只能删除自己组下的用户");
            }
            usersService.removeBatchByIds(ids);
            return Result.defaultSuccessByMessage("操作成功");
        }
    }


    /**
     * 修改权限
     *
     * @return
     */
    @RequiredPermission("system:user:permission")
    @PostMapping("permission")
    public Result<ArrayList<String>> updatePermissions(@RequestBody @Validated PermissionUserVO permissionUserVO) {
        // 权限处理
        List<Long> permissionIds = permissionUserVO.getPermissionIds();
        // 权限更新
        openPermissionService.remove(new LambdaQueryWrapper<OpenPermission>().eq(OpenPermission::getUserId, permissionUserVO.getId()));
        List<OpenPermission> openPermissions = permissionIds.stream().map(permissionId -> {
            OpenPermission openPermission = new OpenPermission();
            openPermission.setPermissionId(permissionId);
            openPermission.setUserId(permissionUserVO.getId());
            return openPermission;
        }).collect(Collectors.toList());
        openPermissionService.saveBatch(openPermissions);
        return Result.defaultSuccessByMessage("操作成功");
    }

    /**
     * 获取接收通知组
     *
     * @return
     */
    @GetMapping("msgGroup")
    public Result<List<MsgGroup>> msgGroup() {
        List<MsgGroup> list = msgGroupService.list();
        return Result.defaultSuccessByMessageAndData("查询成功", list);
    }

    /**
     * 更新接收通知组
     *
     * @return
     */
    @PutMapping("msgGroup")
    public Result<List<MsgGroup>> MsgGroupUpdate(@RequestBody List<MsgUpdateGroupVO> msgUpdateGroupVOs) {
        List<MsgGroup> list1 = msgUpdateGroupVOs.stream().map(msgUpdateGroupVO -> {
            MsgGroup msgGroup = new MsgGroup();
            msgGroup.setId(msgUpdateGroupVO.getId());
            msgGroup.setUserIds(new JSONArray(msgUpdateGroupVO.getUserIds()));
            return msgGroup;
        }).toList();
        msgGroupService.updateBatchById(list1);
        return Result.defaultSuccessByMessage("操作成功");
    }
}
