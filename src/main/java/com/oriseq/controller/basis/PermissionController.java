package com.oriseq.controller.basis;

import cn.hutool.core.util.BooleanUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oriseq.config.LoginTool;
import com.oriseq.config.permission.RequiredPermission;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.controller.utils.Result;
import com.oriseq.controller.utils.RouteUtils;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Permission;
import com.oriseq.dtm.vo.LoginUserVO;
import com.oriseq.dtm.vo.RouteVO;
import com.oriseq.service.IUserGroupService;
import com.oriseq.service.IUsersService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PermissionController
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/9 9:21
 */
@RestController
@Slf4j
public class PermissionController {


    @Autowired
    private IUserGroupService userGroupService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private LoginTool loginTool;

    /**
     * 构建权限树
     *
     * @param permissions
     * @return
     */
    public static List<Permission> buildPermissionTree(List<Permission> permissions) {
        Map<Long, Permission> permissionMap = new HashMap<>();

        // 1. 构建 permissionMap
        for (Permission permission : permissions) {
            permissionMap.put(permission.getId(), permission);
        }

        // 2. 构建树结构
        List<Permission> rootPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            Long parentId = permission.getParentId();
            if (parentId == null) {
                rootPermissions.add(permission);
            } else {
                Permission parent = permissionMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }

        return rootPermissions;
    }

    @GetMapping("/getUserInfo")
    public Result<LoginUserVO> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return Result.defaultErrorByMessage("token不能为空");
        }
        // 手机号码 唯一标识
        String phone = JwtUtils.getUsernameFromToken(token);
        LoginUserVO userVO = usersService.getUserInfo(phone);

        return Result.defaultSuccessByMessageAndData("获取用户信息成功", userVO);
    }

    /**
     * 获取权限码
     *
     * @return
     */
    @GetMapping("/getPermCode")
    public Result<Set<String>> getPermCode() {
        LoginUser userInfo = loginTool.getUserInfo();
        // 用户拥有的权限id
        List<Permission> hasPermissions = userGroupService.getPermissionIds(Long.valueOf(userInfo.getUserId()));
        // 取父级id
        Set<String> permissonIdentifiers = hasPermissions.stream().map(Permission::getPermissionIdentifier).collect(Collectors.toSet());
        if (userInfo.isSuper()) {
            permissonIdentifiers.add("Super");
        }
        return Result.defaultSuccessByMessageAndData("获取权限成功", permissonIdentifiers);
    }

    /**
     * 获取菜单
     *
     * @return
     */
    @GetMapping("/getMenuList")
    public Result getMenuList(HttpServletRequest request) {
        LoginUser user = loginTool.getUserInfo(request);

        // 查权限返回
        List<Permission> permissionByPhone = userGroupService.getMenuPermissionByPhone(user);
        // 排序
        List<Permission> list = permissionByPhone.stream().sorted((o1, o2) -> {
            Integer orderNo1 = o1.getOrderNo();
            Integer orderNo2 = o2.getOrderNo();
            // 如果两个都为空，则认为相等
            if (orderNo1 == null && orderNo2 == null) {
                return 0;
            }
            // 如果o1为空，则o1排在后面
            if (orderNo1 == null) {
                return 1;
            }
            // 如果o2为空，则o2排在后面
            if (orderNo2 == null) {
                return -1;
            }
            // 都不为空，则按照orderNo升序排序
            return orderNo1.compareTo(orderNo2);
        }).filter(permission -> {
            // 过滤禁用的部分菜单
            if (permission.getType().equals("C") && BooleanUtil.isFalse(permission.getEnable())) {
                return false;
            }
            return true;
        }).toList();

        LinkedHashMap<Long, RouteVO> routeVOLinkedHashMap = new LinkedHashMap();
        // 构建最外层的route
        // 菜单较少，这样处理直接
        for (Permission permission : list) {
            if (permission.getParentId() == null) {
                RouteVO routeVO = RouteUtils.getRouteVO(permission);
                routeVOLinkedHashMap.put(permission.getId(), routeVO);
            }
        }

        for (Permission permission : list) {
            if (permission.getParentId() != null) {
                // 构建子菜单
                RouteVO routeVO = RouteUtils.getRouteVO(permission);
                RouteVO vo = routeVOLinkedHashMap.get(permission.getParentId());
                List<RouteVO> children = vo.getChildren();
                children.add(routeVO);
            }
        }
        Collection<RouteVO> values = routeVOLinkedHashMap.values();

        return Result.defaultSuccessByMessageAndData("获取权限成功", values);
    }

    /**
     * 获取按钮权限 及 用户拥有的权限id
     *
     * @return
     */
    @RequiredPermission("system:user:permission")
    @GetMapping("/getPermissionList")
    public Result getPermissionList(@RequestParam("id") Long userId, HttpServletRequest request) {
        LoginUser user = loginTool.getUserInfo(request);
        // 查用户能拥有的权限返回
        List<Permission> permissionByPhone = userGroupService.getButtonPermission(userId);
        // 构建树
        List<Permission> rootPermissions = buildPermissionTree(permissionByPhone);

        // 用户拥有的权限id
        List<Permission> hasPermissions = userGroupService.getPermissionIds(userId);
        // 取父级id
        List<Long> parentIds = hasPermissions.stream().map(Permission::getParentId).toList();
        // 去掉所有父级id的菜单权限，只保留叶子节点，用于树展示。不然会树会错乱全选
        List<Long> permissionIds = hasPermissions.stream()
                .filter(permission -> !parentIds.contains(permission.getId()))
                .map(Permission::getId).collect(Collectors.toList());

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("permissionList", new Gson().toJsonTree(rootPermissions));
        jsonObject.add("permissionIds", new Gson().toJsonTree(permissionIds));
        Map map = new Gson().fromJson(jsonObject, Map.class);
        return Result.defaultSuccessByMessageAndData("获取权限成功", map);
    }

}
