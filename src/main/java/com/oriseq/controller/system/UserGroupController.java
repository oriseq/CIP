package com.oriseq.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.config.PermissionTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.UserGroup;
import com.oriseq.dtm.vo.AddUserGroupVO;
import com.oriseq.dtm.vo.UserGroupSelectVO;
import com.oriseq.service.IUserGroupService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户组管理
 *
 * @author hacah
 * @version 1.0
 */
@RestController
@RequestMapping("system/userGroup")
@Slf4j
@EnableLogging
public class UserGroupController {


    @Autowired
    private IUserGroupService userGroupService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private PermissionTool permissionTool;


    @GetMapping("list")
    public Result<List<UserGroup>> allUserGroup(
            @RequestParam(value = "groupName", required = false) String groupName,
            HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 非超级管理员，无权限
        if (!userInfo.isSuper()) {
            return Result.defaultErrorByMessage("无权限操作");
        }
        LambdaQueryWrapper<UserGroup> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(StringUtils.isNotBlank(groupName), UserGroup::getGroupName, groupName);
        List<UserGroup> groupList = userGroupService.list(userLambdaQueryWrapper);
        return Result.defaultSuccessByMessageAndData("查询成功", groupList);
    }


    /**
     * 新增/更新用户组
     *
     * @return
     */
    @PostMapping("userGroup")
    public Result<ArrayList<String>> addOrUpdateUserGroup(@RequestBody @Validated AddUserGroupVO addUserGroupVO, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 非超级管理员，无权限
        if (!userInfo.isSuper()) {
            return Result.defaultErrorByMessage("无权限操作");
        }
        // 是否更新
        if (addUserGroupVO.getId() != null) {
            UserGroup userGroup = new UserGroup();
            userGroup.setId(addUserGroupVO.getId());
            userGroup.setGroupName(addUserGroupVO.getGroupName());
            userGroup.setAvailStatus(addUserGroupVO.getAvailStatus());
            userGroup.setIsInternalGroup(addUserGroupVO.getIsInternalGroup());
            userGroupService.updateById(userGroup);
            userGroupService.deleteUserInfoCacheOfGroup(loginTool, addUserGroupVO.getId());
            return Result.defaultSuccessByMessageAndData("更新成功", null);
        }
        // 1. 检查是否存在相同用户组
        long count = userGroupService.count(new QueryWrapper<UserGroup>().lambda()
                .eq(UserGroup::getGroupName, addUserGroupVO.getGroupName()));
        // 判断哪个重复
        if (count > 0) {
            return Result.defaultErrorByMessageAndData("用户组已存在", null);
        }
        // 4. 存储用户组
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupName(addUserGroupVO.getGroupName());
        userGroup.setIsInternalGroup(addUserGroupVO.getIsInternalGroup());
        userGroupService.save(userGroup);
        return Result.defaultSuccessByMessage("添加成功");
    }


    /**
     * 删除用户组
     *
     * @return
     */
    @DeleteMapping("userGroup")
    public Result<ArrayList<String>> deleteUserGroup(@RequestBody List<String> ids, HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        // 非超级管理员，无权限
        if (!userInfo.isSuper()) {
            return Result.defaultErrorByMessage("无权限操作");
        }
        userGroupService.removeBatchByIds(ids);
        return Result.defaultSuccessByMessage("操作成功");
    }


    /**
     * 用于选择器的组数据
     *
     * @param scene 如果为空，则返回所有组，不为空，则返回指定场景下的组
     *              inspectionUnit：非内部组数据
     *              fillIn：送检填写使用：非内部组数据&非禁用组
     * @return
     */
    @GetMapping("groupSelect")
    public Result<List<UserGroupSelectVO>> groupSelect(@RequestParam(value = "scene", required = false) String scene) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<UserGroup> groupList = userGroupService.list();
        if (StringUtils.isNotBlank(scene)) {
            if (scene.equals("inspectionUnit")) {
                // 根据场景过滤
                groupList = groupList.stream().filter(userGroup -> {
                    return !userGroup.getIsInternalGroup();
                }).toList();
            } else if (scene.equals("fillIn")) {
                groupList = groupList.stream().filter(userGroup -> {
                    return !userGroup.getIsInternalGroup() && userGroup.getAvailStatus();
                }).toList();
            }
        }
        List<UserGroupSelectVO> list = groupList.stream()
                .sorted((a, b) -> {
                    // 内部组排在后面
                    if (a.getIsInternalGroup() == null || b.getIsInternalGroup() == null) {
                        return 0;
                    }
                    return a.getIsInternalGroup().compareTo(b.getIsInternalGroup());
                }).map(userGroup -> {
                    UserGroupSelectVO userGroupSelectVO = new UserGroupSelectVO();
                    userGroupSelectVO.setId(userGroup.getId());
                    userGroupSelectVO.setGroupName(userGroup.getGroupName());
                    return userGroupSelectVO;
                })
                .toList();
        // 非超级管理员且 不属于内部组，只能自己组的信息
        if (!userInfo.isSuper() && !userInfo.getIsInternalGroup()) {
            // 获取自己组信息
            List<UserGroupSelectVO> selectVOS = list.stream().filter(userGroupSelectVO -> userGroupSelectVO.getId().equals(userInfo.getUserGroupId())).toList();
            return Result.defaultSuccessByMessageAndData("查询成功", selectVOS);
        }
        return Result.defaultSuccessByMessageAndData("查询成功", list);
    }

}
