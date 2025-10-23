package com.oriseq.controller.system;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.innerProject.InnerProjectSearchDTO;
import com.oriseq.dtm.entity.Project;
import com.oriseq.dtm.entity.ProjectClassification;
import com.oriseq.dtm.vo.innerProject.InnerProjectDelVO;
import com.oriseq.dtm.vo.project.ProjectSearchVO;
import com.oriseq.service.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 内部项目管理
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/31 14:07
 */
@RestController
@RequestMapping("system/innerProject")
@Slf4j
@EnableLogging
public class InnerProjectController {

    @Autowired
    private IUsersService usersService;
    @Autowired
    private LoginTool loginTool;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IPackageService packageService;
    @Autowired
    private ProjectReferencesService projectReferencesService;
    @Autowired
    private IProjectClassificationService projectClassificationService;

    /**
     * 构建树结构的list
     *
     * @param list
     * @return
     */
    @NotNull
    private static JSONArray buildTreeList(List<ProjectClassification> list) {
        /*
        [
            {
                "id": 16,
                "value": 16,
                "label": "分类1",
                "children": [
                    {
                        "id": 9,
                        "value": 9,
                        "label": "子分类2"
                    },
                    {
                        "id": 11,
                        "value": 11,
                        "label": "子分类3"
                    }
                ]
            },
            {
                "id": 2,
                "value": 2,
                "label": "分类4",
                "children": [
                    {
                        "id": 10,
                        "value": 10,
                        "label": "子分类5"
                    }
                ]
            }
        ]
         */
        // 作为root节点
        List<ProjectClassification> rootNodes = list.stream().filter(node -> node.getParentId() == null).toList();
        JSONArray array = new JSONArray();
        for (ProjectClassification rootNode : rootNodes) {
            JSONObject entries = new JSONObject();
            entries.set("id", rootNode.getId());
            entries.set("label", rootNode.getName());
            entries.set("value", rootNode.getId());
            List<JSONObject> childrenNodes = list.stream()
                    .filter(node -> node.getParentId() != null && node.getParentId().equals(rootNode.getId()))
                    .map(node -> {
                                JSONObject entries1 = new JSONObject();
                                entries1.set("id", node.getId());
                                entries1.set("label", node.getName());
                                entries1.set("value", node.getId());
                                return entries1;
                            }
                    ).toList();
            entries.set("children", childrenNodes);
            array.add(entries);
        }
        return array;
    }

    //    @RequiredPermission("system:innerProject:query")
    @PostMapping("list")
    public List<InnerProjectSearchDTO> project(@RequestBody ProjectSearchVO projectSearchVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        // 条件
        // 过滤项目引用ids
        if (projectSearchVO.getProjectIds() != null && !projectSearchVO.getProjectIds().isEmpty()) {
            queryWrapper.in("pm.id", projectSearchVO.getProjectIds());
        }
        List<InnerProjectSearchDTO> innerProjectSearchDTOS = projectService.extendList(queryWrapper);
        return innerProjectSearchDTOS;
    }

    /**
     * 新增
     *
     * @param project
     * @return
     */
//    @RequiredPermission("system:innerProject:add")
    @PostMapping("")
    public Result<Object> add(@Validated @RequestBody Project project) {
        projectService.save(project);
        return Result.defaultSuccessByMessage("新增成功");
    }

    /**
     * 更新
     *
     * @param project
     * @return
     */
//    @RequiredPermission("system:innerProject:update")
    @PutMapping("")
    public Result<Object> update(@Validated @RequestBody Project project) {
        projectService.updateById(project);
        return Result.defaultSuccessByMessage("更新成功");
    }

    /**
     * 删除
     *
     * @param innerProjectDelVO
     * @return
     */
//    @RequiredPermission("system:innerProject:delete")
    @DeleteMapping("")
    public Result<Object> delete(@Validated @RequestBody InnerProjectDelVO innerProjectDelVO) {
        List<Long> projectIds = innerProjectDelVO.getProjectIds();
        boolean removeBatchByIds = projectService.removeBatchByIds(projectIds);
        return Result.defaultSuccessByMessage("删除成功");
    }

    /**
     * 获取项目分类树结构
     *
     * @return
     */
    @GetMapping("projectClassification")
    public Result<JSONArray> projectClassification() {
        List<ProjectClassification> list = projectClassificationService.list();
        JSONArray array = buildTreeList(list);
        return Result.defaultSuccessByMessageAndData("获取成功", array);
    }

    /**
     * 获取检测方法list
     *
     * @return
     */
    @GetMapping("detectMethod")
    public Result<Set<String>> detectMethod() {
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.select(Project::getDetectMethod);
        // TODO 优化
        List<Project> list = projectService.list(projectLambdaQueryWrapper);
        Set<String> strings = list.stream().filter(project -> project != null && !"".equals(project.getDetectMethod())).map(project -> project.getDetectMethod()).collect(Collectors.toSet());
        return Result.defaultSuccessByMessageAndData("获取成功", strings);
    }

    /**
     * 获取耗材类型list
     *
     * @return
     */
    @GetMapping("consumableType")
    public Result<Set<String>> consumableType() {
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.select(Project::getConsumableType);
        // TODO 优化
        List<Project> list = projectService.list(projectLambdaQueryWrapper);
        Set<String> strings = list.stream().filter(project -> project != null && !"".equals(project.getDetectMethod())).map(project -> project.getConsumableType()).collect(Collectors.toSet());
        return Result.defaultSuccessByMessageAndData("获取成功", strings);
    }


}
