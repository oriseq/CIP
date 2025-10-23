package com.oriseq.controller.sample;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.entity.Sample;
import com.oriseq.dtm.vo.InspectionInfoVO;
import com.oriseq.dtm.vo.PackageVO;
import com.oriseq.service.IPackageService;
import com.oriseq.service.IProjectService;
import com.oriseq.service.ISampleService;
import com.oriseq.service.MessageNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 送检单提交
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:10
 */
@RestController
@RequestMapping("sample")
@EnableLogging
public class InspectionFormController {

    @Autowired
    private IPackageService packageService;

    @Autowired
    private ISampleService sampleService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private LoginTool loginTool;

    @Autowired
    private MessageNotificationService messageNotificationService;

    /**
     * 获取套餐
     *
     * @return
     */
    @GetMapping("combos")
    public Result<List<PackageVO>> getCombos(HttpServletRequest request) {
        LoginUser userInfo = loginTool.getUserInfo(request);
        List<PackageVO> packageVOS = packageService.getPackageInfo(userInfo);
        if (packageVOS == null || packageVOS.size() == 0) {
            return Result.defaultSuccessByMessageAndData("获取套餐成功", List.of());
        }
        // 排序  存在《上次提交的项目组合》排前面  然后 通用套餐在前面
        packageVOS = packageVOS.stream().sorted(Comparator.comparing(p -> !p.getPackageName().equals("上次提交的项目组合"))).collect(Collectors.toList());
        return Result.defaultSuccessByMessageAndData("获取套餐成功", packageVOS);
    }

    /**
     * 送检单提交
     *
     * @param inspectionInfoVO
     */
    @PutMapping("inspectionForm")
    public Result<InspectionInfoVO> submitInspectionForm(@Validated @RequestBody InspectionInfoVO inspectionInfoVO, BindingResult bindingResult, HttpServletRequest request) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> collect = fieldErrors.stream().map(e -> e.getField() + ":" + e.getDefaultMessage()).collect(Collectors.toList());
        if (collect.size() > 0) {
            return Result.defaultErrorByMessage(collect.toString());
        }

        LoginUser userInfo = loginTool.getUserInfo(request);
        // 处理外送单位为自检状态的提交，默认为空，空就是自检
        if (inspectionInfoVO.getSelectedProjects() != null) {
            inspectionInfoVO.getSelectedProjects().stream()
                    .filter(Objects::nonNull) // 避免空项目导致 NullPointerException
                    .filter(project -> "自检".equals(project.getDeliveryUnit()))
                    .forEach(project -> project.setDeliveryUnit(null));
        }
        // 提交
        Sample sample = sampleService.submitInspectionForm(inspectionInfoVO, userInfo);

        // 发送样本提交通知
        messageNotificationService.sendNotificationsWhenSampleSubmission(
                StrUtil.isNotBlank(userInfo.getRealName()) ? userInfo.getRealName() : userInfo.getUsername()
                , String.valueOf(sample.getId()), sample.getName());

        return Result.defaultSuccessByMessage("提交成功");
    }

    /**
     * 获取所有项目,根据用户组限制
     *
     * @return
     */
    @GetMapping("projects")
    public Result<List<Map>> getClassProjects(HttpServletRequest request) throws JsonProcessingException {
        LoginUser userInfo = loginTool.getUserInfo(request);
        Collection<JsonObject> projects = projectService.getProjectsByUserGroup(userInfo);
        if (projects == null && projects.size() == 0) {
            throw new RuntimeException("项目信息不存在");
        }
        List<Map> collect = projects.stream().map(jsonObject -> {
            Gson gson = new Gson();
            return gson.fromJson(jsonObject, Map.class);
        }).collect(Collectors.toList());
        return Result.defaultSuccessByMessageAndData("获取项目成功", collect);
    }

    /**
     * 获取所有项目,不作用户组限制
     *
     * @return
     */
    @GetMapping("AllProjects")
    public Result<List<Map>> getClassAllProjects() throws JsonProcessingException {
        LoginUser userInfo = loginTool.getUserInfo();
        Collection<JsonObject> projects = projectService.getAllProjectsByUserGroup(userInfo);
        if (projects == null && projects.size() == 0) {
            throw new RuntimeException("项目信息不存在");
        }
        List<Map> collect = projects.stream().map(jsonObject -> {
            Gson gson = new Gson();
            return gson.fromJson(jsonObject, Map.class);
        }).collect(Collectors.toList());
        return Result.defaultSuccessByMessageAndData("获取项目成功", collect);
    }


    /**
     * 小程序-获取所有项目，只显示一层分类
     *
     * @return
     */
    @GetMapping("classprojects")
    public Result<List<Map>> getClassProjectsForSmallProcedures(HttpServletRequest request) throws JsonProcessingException {
        LoginUser userInfo = loginTool.getUserInfo(request);
        Collection<JsonObject> projects = projectService.getOneClassificationProjectsByUserGroup(userInfo);
        if (projects == null && projects.size() == 0) {
            throw new RuntimeException("项目信息不存在");
        }
        List<Map> collect = projects.stream().map(jsonObject -> {
            Gson gson = new Gson();
            return gson.fromJson(jsonObject, Map.class);
        }).collect(Collectors.toList());
        return Result.defaultSuccessByMessageAndData("获取项目成功", collect);
    }

}
