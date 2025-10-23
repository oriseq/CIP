package com.oriseq.controller.system;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oriseq.config.LoginTool;
import com.oriseq.config.PermissionTool;
import com.oriseq.config.permission.RequiredPermission;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.PackageManageInfoDTO;
import com.oriseq.dtm.dto.ProjectPriceDTO;
import com.oriseq.dtm.entity.Package;
import com.oriseq.dtm.vo.packageInfo.PackageDeleteVO;
import com.oriseq.dtm.vo.packageInfo.PackageSearchVO;
import com.oriseq.dtm.vo.packageInfo.PackageUpdateVO;
import com.oriseq.service.IPackageService;
import com.oriseq.service.ProjectReferencesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 用户组管理
 *
 * @author hacah
 * @version 1.0
 */
@RestController
@RequestMapping("system/package")
@Slf4j
public class PackageController {


    @Autowired
    private IPackageService packageService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private PermissionTool permissionTool;

    @Autowired
    private ProjectReferencesService projectReferencesService;


    @RequiredPermission("system:package:query")
    @PostMapping("list")
    public Result<List<JSONObject>> getPackages(@RequestBody PackageSearchVO packageSearchVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<PackageManageInfoDTO> packageMangInfos = packageService.getPackageManageInfo(userInfo, packageSearchVO);
        // 去掉projectIdList，处理，设置套餐总价格 和 总折扣价,并装换成json
        List<JSONObject> jsonObjectList = packageMangInfos.stream().map(item -> {
            String projectIdList = item.getProjectIdList();
            item.setProjectIdList(null);
            List<String> packageProjectIdList = Arrays.stream(projectIdList.split(",")).toList();
            JSONObject entries = JSONUtil.parseObj(item);
            // 项目id
            entries.set("projectIds", packageProjectIdList);
            // 设置套餐总价格 和 总折扣价
            ProjectPriceDTO projectPrice = projectReferencesService.getSUMProjectPrice(
                    new QueryWrapper<>().in("pm.Id", packageProjectIdList),
                    new QueryWrapper<>().in("pr.project_id", packageProjectIdList),
                    item.getUserGroupId());
            if (Objects.nonNull(projectPrice)) {
                if (userInfo.isSuper()) {
                    entries.set("totalPrice", projectPrice.getTotalPrice());
                    entries.set("totalDiscountedPrice", projectPrice.getTotalDiscountedPrice());
                } else {
                    entries.set("totalPrice", projectPrice.getTotalPrice());
                }
            }
            return entries;
        }).toList();
        return Result.defaultSuccessByMessageAndData("获取套餐成功", jsonObjectList);
    }

    @RequiredPermission("system:package:add")
    @PostMapping("")
    public Result<List<JSONObject>> addPackageInfo(@Validated @RequestBody PackageSearchVO packageSearchVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<Long> projectIds = packageSearchVO.getProjectIds();
        // 转换成string类型
        List<String> projectIdsStr = projectIds.stream().map(String::valueOf).toList();
        String join = String.join(",", projectIdsStr);
        Package aPackage = new Package();
        aPackage.setPackageInfo(packageSearchVO.getPackageName());
        aPackage.setUserGroupId(userInfo.getUserGroupId());
        aPackage.setProjectIdList(join);
        aPackage.setRemarks(packageSearchVO.getRemarks());
        packageService.save(aPackage);
        return Result.defaultSuccessByMessage("新增套餐成功");
    }

    @RequiredPermission("system:package:update")
    @PutMapping("")
    public Result<List<JSONObject>> updatePackageInfo(@Validated @RequestBody PackageUpdateVO packageUpdateVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<Long> projectIds = packageUpdateVO.getProjectIds();
        // 转换成string类型
        List<String> projectIdsStr = projectIds.stream().map(String::valueOf).toList();
        String join = String.join(",", projectIdsStr);
        Package aPackage = new Package();
        aPackage.setPackageInfo(packageUpdateVO.getPackageName());
        aPackage.setUserGroupId(userInfo.getUserGroupId());
        aPackage.setProjectIdList(join);
        aPackage.setId(packageUpdateVO.getId());
        aPackage.setRemarks(packageUpdateVO.getRemarks());
        // 权限控制，暂定超级管理员和内部组可以修改套餐用户组信息
        // 判断套餐用户组是否和原来的信息一致
        Package byId = packageService.getById(packageUpdateVO.getId());
        if (!byId.getUserGroupId().equals(packageUpdateVO.getUserGroupId())) {
            if (!userInfo.isSuper() && !userInfo.getIsInternalGroup()) {
                log.error("无权限操作, 只有超级管理员和内部组可以修改套餐用户组信息");
                return Result.defaultErrorByMessage("无权限操作");
            } else {
                aPackage.setUserGroupId(packageUpdateVO.getUserGroupId());
            }
        }
        packageService.updateById(aPackage);
        return Result.defaultSuccessByMessage("修改套餐成功");
    }


    @RequiredPermission("system:package:delete")
    @DeleteMapping("")
    public Result<List<JSONObject>> deletePackageInfo(@Validated @RequestBody PackageDeleteVO packageDeleteVO) {
//        LoginUser userInfo = loginTool.getUserInfo();
        List<Long> packageIds = packageDeleteVO.getPackageIds();
        packageService.removeBatchByIds(packageIds);
        return Result.defaultSuccessByMessage("删除成功");
    }


}
