package com.oriseq.controller.system;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.config.permission.RequiredPermission;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectListDTO;
import com.oriseq.dtm.entity.Project;
import com.oriseq.dtm.entity.ProjectReferences;
import com.oriseq.dtm.vo.project.*;
import com.oriseq.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 引用项目管理
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/31 14:07
 */
@RestController
@RequestMapping("system/project")
@Slf4j
public class ProjectReferController {

    @Autowired
    private IUsersService usersService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private ProjectReferencesService projectReferencesService;
    @Autowired
    private IPackageService packageService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private MessageNotificationService messageNotificationService;


    @RequiredPermission("system:cooperateProject:query")
    @PostMapping("list")
    @EnableLogging
    public List<ProjectListDTO> project(@RequestBody ProjectSearchVO projectSearchVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        // 数据区分超级管理员和普通管理员
        List<ProjectListDTO> projects = projectReferencesService.getProjects(userInfo, projectSearchVO);
        return projects;
    }


    /**
     * 获取套餐，包含用户组信息
     *
     * @return
     */
    @RequiredPermission("system:cooperateProject:query")
    @GetMapping("belongingPackageCascader")
    public Collection belongingPackageCascader() {
        LoginUser userInfo = loginTool.getUserInfo();
        Collection<JSONObject> belongingPackageCascader = packageService.getBelongingPackageCascader(userInfo);
        if (!userInfo.isSuper()) {
            List<Object> collect = belongingPackageCascader.stream().flatMap(item -> {
                JSONArray jsonArray = item.getJSONArray("children");
                return jsonArray.stream();
            }).collect(Collectors.toList());
            return collect;
        }
        return belongingPackageCascader;
    }

    /**
     * 更新
     *
     * @param projectUpdateVO
     * @return
     */
    @EnableLogging
    @RequiredPermission("system:cooperateProject:update")
    @PutMapping("")
    public Result<Object> update(@Validated @RequestBody ProjectUpdateVO projectUpdateVO) {
        Long projectRefId = projectUpdateVO.getProjectId();

        // 修改价格
//        Project project = new Project(); ProjectReferences byId = projectReferencesService.getById(projectRefId);
        // 修改默认送检单位
        ProjectReferences projectReferences = new ProjectReferences();
        projectReferences.setPriceCoefficient(projectUpdateVO.getPriceCoefficient());
        // 使用 updateWrapper 显式设置为 null 当getDefaultDeliveryUnitId为null时
        LambdaUpdateWrapper<ProjectReferences> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProjectReferences::getId, projectRefId)
                .set(ProjectReferences::getDefaultDeliveryUnitId, projectUpdateVO.getDefaultDeliveryUnitId());
        projectReferencesService.update(projectReferences, updateWrapper);
//        projectService.updateById(project);
        return Result.defaultSuccessByMessage("修改成功");
    }

    /**
     * 删除
     *
     * @param projectDelVO
     * @return
     */
    @EnableLogging
    @RequiredPermission("system:cooperateProject:delete")
    @DeleteMapping("")
    public Result<Object> delete(@Validated @RequestBody ProjectDelVO projectDelVO) {
        // 修改价格
        List<Long> projectIds = projectDelVO.getProjectIds();
        boolean removeBatchByIds = projectReferencesService.removeBatchByIds(projectIds);
        return Result.defaultSuccessByMessage("删除成功");
    }

    /**
     * 项目申请
     *
     * @param projectApplicationVO
     * @return
     */
    @EnableLogging
    @PostMapping("projectApplication")
    public Result<Object> projectApplication(@Validated @RequestBody ProjectApplicationVO projectApplicationVO) {
        // TODO 项目申请具体操作
        LoginUser userInfo = loginTool.getUserInfo();
        String userGroupName = userInfo.getUserGroupName();
        messageNotificationService.sendNotificationsWhenApplyForANewCollaborativeProject(userGroupName);
        return Result.defaultSuccessByMessage("申请成功");
    }

    /**
     * 新增
     *
     * @param projectAddVO
     * @return
     */
    @EnableLogging
//    @RequiredPermission("system:project:add")
    @PostMapping("")
    public Result<Object> add(@Validated @RequestBody ProjectAddVO projectAddVO) {
        List<ProjectReferences> list = projectAddVO.getProjectIds().stream().map(projectId -> {
            ProjectReferences projectReferences = new ProjectReferences();
            projectReferences.setProjectId(projectId);
            projectReferences.setUserGroupId(projectAddVO.getUserGroupId());
            projectReferences.setPriceCoefficient(projectAddVO.getPriceCoefficient());
            projectReferences.setDefaultDeliveryUnitId(projectAddVO.getDefaultDeliveryUnitId());
            return projectReferences;
        }).toList();
        // 查出所有的project，确认project中deliveryUnitIds是否包含有projectAddVO.getDefaultDeliveryUnitId()，如果没有就加上，之后统一更新
        List<Project> projects = projectService.listByIds(projectAddVO.getProjectIds());
        Long defaultDeliveryUnitId = projectAddVO.getDefaultDeliveryUnitId();

        projects.forEach(project -> {
            if (project.getDeliveryUnitIds() == null) {
                project.setDeliveryUnitIds(new JSONArray());
            }
            if (!project.getDeliveryUnitIds().contains(defaultDeliveryUnitId)) {
                project.getDeliveryUnitIds().add(defaultDeliveryUnitId);
            }
        });

        try {
            projectService.updateBatchById(projects);
            projectReferencesService.saveOrUpdateBatch(list);
        } catch (DuplicateKeyException e1) {
            e1.printStackTrace();
            return Result.defaultErrorByMessage("部分项目已存在，请检查提交的项目");
        }
        return Result.defaultSuccessByMessage("新增成功");
    }

    /**
     * 获取分组下的所有项目ids
     *
     * @param projectIdsByGroupVO
     * @return
     */
    @PostMapping("projectIdsByGroup")
    public Result<Object> projectIdsByGroup(@Validated @RequestBody ProjectIdsByGroupVO projectIdsByGroupVO) {
        LambdaQueryWrapper<ProjectReferences> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ProjectReferences::getProjectId);
        wrapper.eq(ProjectReferences::getUserGroupId, projectIdsByGroupVO.getUserGroupId());
        List<ProjectReferences> list = projectReferencesService.list(wrapper);
        List<Long> projectIds = list.stream().map(ProjectReferences::getProjectId).toList();
        return Result.defaultSuccessByMessageAndData("查询成功", projectIds);
    }


}
