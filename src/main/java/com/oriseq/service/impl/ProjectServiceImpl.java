package com.oriseq.service.impl;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oriseq.common.business.statisticalProject.StatisticalProjectRedisTool;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectDTO;
import com.oriseq.dtm.dto.innerProject.InnerProjectSearchDTO;
import com.oriseq.dtm.entity.DeliveryUnit;
import com.oriseq.dtm.entity.Project;
import com.oriseq.dtm.entity.ProjectClassification;
import com.oriseq.dtm.entity.ProjectReferences;
import com.oriseq.dtm.vo.ProjectVO;
import com.oriseq.dtm.vo.ProjectVOForWX;
import com.oriseq.mapper.ProjectClassificationMapper;
import com.oriseq.mapper.ProjectMapper;
import com.oriseq.service.DeliveryUnitService;
import com.oriseq.service.IProjectService;
import com.oriseq.service.ProjectReferencesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:37
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    private ProjectClassificationMapper projectClassificationMapper;
    @Autowired
    private StatisticalProjectRedisTool statisticalProjectRedisTool;
    @Autowired
    private DeliveryUnitService deliveryUnitService;
    @Autowired
    private ProjectReferencesService projectReferencesService;

    /**
     * 对树进行排序（包括根节点和子节点）
     *
     * @return
     */
    public static List<JsonObject> sortTree(Collection<JsonObject> nodes) {
        // 对根节点进行排序
        List<JsonObject> nodeList = new ArrayList<>(nodes);
        nodeList.sort(Comparator.comparingLong(
                node -> -node.get("submissionTimes").getAsLong() // 降序排序
        ));

        // 对每个节点的子节点递归排序
        for (JsonObject node : nodeList) {
            if (node.has("children")) {
                JsonArray children = node.getAsJsonArray("children");
                // 对子节点排序
                JsonArray sortedChildren = sortChildren(children);
                // 替换原有的 children
                node.add("children", sortedChildren);
            }
        }
        return nodeList;
    }

    /**
     * 对子节点进行排序
     */
    private static JsonArray sortChildren(JsonArray children) {
        // 将子节点转换为 List<JsonObject>
        List<JsonObject> childList = new ArrayList<>();
        for (JsonElement child : children) {
            childList.add(child.getAsJsonObject());
        }

        // 按 submissionTimes 降序排序
        childList.sort(Comparator.comparingLong(
                child -> -child.get("submissionTimes").getAsLong() // 降序排序
        ));

        // 创建新的 JsonArray 并添加排序后的子节点
        JsonArray sortedChildren = new JsonArray();
        for (JsonObject child : childList) {
            sortedChildren.add(child);
            // 递归排序子节点的子节点
            if (child.has("children")) {
                JsonArray grandChildren = sortChildren(child.getAsJsonArray("children"));
                child.add("children", grandChildren);
            }
        }

        return sortedChildren;
    }

    @Override
    public Collection<JsonObject> getProjectsByUserGroup(LoginUser userInfo) {
        // 查询该用户能看的所有项目
        boolean isAll = userInfo.getIsInternalGroup() || userInfo.isSuper();
        List<ProjectDTO> projects = baseMapper.selectAllProjectsByUserGroupId(isAll ? 1 : 0, userInfo.getUserGroupId());
        // 1.设置DeliveryUnitSetting的defaultOptions，先查询所有外送单位
        List<DeliveryUnit> deliveryUnits = deliveryUnitService.list();
        // 创建外送单位ID到名称的映射
        Map<Long, String> deliveryUnitMap = deliveryUnits.stream()
                .collect(Collectors.toMap(DeliveryUnit::getId, DeliveryUnit::getName));

        // 查询ProjectReferences,通过数据包含projects的id
        List<ProjectReferences> projectReferences = projectReferencesService.list(new LambdaQueryWrapper<ProjectReferences>().in(ProjectReferences::getProjectId,
                projects.stream().map(ProjectDTO::getId).collect(Collectors.toList())));
        // 创建项目ID到ProjectReferences列表的映射
        Map<Long, List<ProjectReferences>> projectReferencesMap = projectReferences.stream()
                .collect(Collectors.groupingBy(ProjectReferences::getProjectId));

        // 设置 deliveryUnitSetting 到每个 ProjectDTO
        for (ProjectDTO projectDTO : projects) {
            if (projectDTO.getDeliveryUnitIds() != null) {
                List<Long> deliveryUnitIds = projectDTO.getDeliveryUnitIds().toList(Long.class);

                // 设置 options
                // 添加"自检"到第一位
                List<String> options = Stream.concat(
                        Stream.of("自检"),
                        deliveryUnitIds.stream()
                                .map(deliveryUnitMap::get)
                                .filter(Objects::nonNull)).collect(Collectors.toList());


                // 设置 defaultOptions
                Map<String, String> defaultOptions = new HashMap<>();
                List<ProjectReferences> projectReferencesList = projectReferencesMap.get(projectDTO.getId());
                if (projectReferencesList != null) {
                    for (ProjectReferences projectReference : projectReferencesList) {
                        Long defaultDeliveryUnitId = projectReference.getDefaultDeliveryUnitId();
                        if (defaultDeliveryUnitId != null) {
                            String defaultDeliveryUnitName = deliveryUnitMap.get(defaultDeliveryUnitId);
                            if (defaultDeliveryUnitName != null) {
                                defaultOptions.put(String.valueOf(projectReference.getUserGroupId()), defaultDeliveryUnitName);
                            }
                        }
                    }
                }

                ProjectDTO.DeliveryUnitSetting deliveryUnitSetting = new ProjectDTO.DeliveryUnitSetting();
                deliveryUnitSetting.setOptions(options);
                deliveryUnitSetting.setDefaultOption(defaultOptions);
                projectDTO.setDeliveryUnitSetting(deliveryUnitSetting);
            }
        }


        List<ProjectClassification> projectClassificationList = projectClassificationMapper.selectList(null);
        return buildTree(userInfo.getUserGroupId(), projectClassificationList, projects);
    }

    @NotNull
    private Collection<JsonObject> buildTree(Long userGroupId, List<ProjectClassification> projectClassificationList, List<ProjectDTO> projects) {
        // 按 parentId 是否为 null 分组：根节点和子节点
        Map<Boolean, List<ProjectClassification>> groupedProjects = projectClassificationList.stream()
                .collect(Collectors.partitioningBy(classification -> classification.getParentId() == null));
        // 先处理根节点
        List<ProjectClassification> rootClassification = groupedProjects.get(true);
        // 再处理子节点:二级节点
        List<ProjectClassification> childClassification = groupedProjects.get(false);
        // 创建 分类key：List<Project>  value只包含项目类型
        Map<Object, Object> groupProjectCount = statisticalProjectRedisTool.getGroupProjectCount(userGroupId);
        Map<Long, List<ProjectVO>> collect = projects.stream().collect(Collectors.toMap(
                ProjectDTO::getPcId,
                projectDTO -> {
                    // 不是项目类型
                    if (projectDTO.getId() == null) {
                        return new ArrayList<>();
                    }
                    ProjectVO projectVO = new ProjectVO();
                    projectVO.setName(projectDTO.getProjectName());
                    projectVO.setId(projectDTO.getId());
                    // 默认 状态
                    projectVO.setStatus(2);
                    projectVO.setConsumableType(projectDTO.getConsumableType());
                    projectVO.setDeliveryUnit(projectDTO.getDeliveryUnit());
                    projectVO.setDeliveryUnitSetting(projectDTO.getDeliveryUnitSetting());
                    // 添加 submissionTimes
                    Object o = groupProjectCount.getOrDefault(projectVO.getId().toString(), "0");
//                    Long projectCount = 0L;
                    long projectCount = Long.parseLong(o.toString());
                    projectVO.setSubmissionTimes(projectCount);
                    return new ArrayList<>(List.of(projectVO));
                }, (oldValue, newValue) -> {
                    oldValue.addAll(newValue);
                    return oldValue;
                }));
        // 生成树
        HashMap<Long, JsonObject> rootList = new HashMap<>();
        // 先处理根节点
        for (ProjectClassification classification : rootClassification) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "c-" + classification.getId());
            jsonObject.addProperty("name", classification.getName());
            jsonObject.addProperty("submissionTimes", 0); // 初始化为0，后续会计算
            List<ProjectVO> projectList = collect.get(classification.getId());
            if (!projectList.isEmpty()) {
                jsonObject.add("children", new Gson().toJsonTree(projectList));
                // 计算根节点的 submissionTimes
                long sum = projectList.stream().mapToLong(ProjectVO::getSubmissionTimes).sum();
                jsonObject.addProperty("submissionTimes", sum);
            } else {
                JsonArray jsonArray = new JsonArray();
                jsonObject.add("children", jsonArray);
            }
            rootList.put(classification.getId(), jsonObject);
        }

        // 再处理子节点
        for (ProjectClassification classification : childClassification) {
            JsonObject parentProjectVO = rootList.get(classification.getParentId());
            if (parentProjectVO != null) {
                // 分类下的项目
                List<ProjectVO> projectList = collect.get(classification.getId());
                if (!projectList.isEmpty()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", "c-" + classification.getId());
                    jsonObject.addProperty("name", classification.getName());
                    // 计算当前节点的 submissionTimes
                    long sum = projectList.stream().mapToLong(ProjectVO::getSubmissionTimes).sum();
                    jsonObject.addProperty("submissionTimes", sum);
                    jsonObject.add("children", new Gson().toJsonTree(projectList));
                    // 给父节点添加子节点
                    JsonArray childrenArray = parentProjectVO.get("children").getAsJsonArray();
                    childrenArray.add(jsonObject);
                    // 更新父节点的 submissionTimes
                    long parentSum = parentProjectVO.get("submissionTimes").getAsLong();
                    parentProjectVO.addProperty("submissionTimes", parentSum + sum);

                }

            }
        }

        // 对树进行排序
        List<JsonObject> jsonObjects = sortTree(rootList.values());

        // 去除没有children的分支
//        Collection<JsonObject> values = rootList.values().stream()
//                .filter(jsonObject -> jsonObject.has("children")).collect(Collectors.toList());
        return jsonObjects;
    }

    @Override
    public Collection<JsonObject> getOneClassificationProjectsByUserGroup(LoginUser userInfo) {
        // 查询该用户能看的所有项目
        List<ProjectDTO> projects = baseMapper.selectAllProjectsByUserGroupId(userInfo.getIsSuper(), userInfo.getUserGroupId());
        // 创建 分类key：List<Project>
        Map<Long, List<ProjectVOForWX>> collect = projects.stream().collect(Collectors.toMap(ProjectDTO::getPcId, projectDTO -> {
            ProjectVOForWX projectVO = new ProjectVOForWX();
            projectVO.setName(projectDTO.getProjectName());
            projectVO.setId(projectDTO.getId());
            // 默认 状态
            projectVO.setStatus("");
            projectVO.setIconValue(true);
            projectVO.setConsumableType(projectDTO.getConsumableType());
            if (projectVO.getId() == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(List.of(projectVO));
        }, (oldValue, newValue) -> {
            oldValue.addAll(newValue);
            return oldValue;
        }));

        //
        LinkedHashMap<Object, JsonObject> rootList = new LinkedHashMap<>();
        for (ProjectDTO project : projects) {
            List<ProjectVOForWX> projectList = collect.get(project.getPcId());
            if (!projectList.isEmpty()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", "c-" + project.getPcId());
                jsonObject.addProperty("name", project.getName());
                jsonObject.add("children", new Gson().toJsonTree(collect.get(project.getPcId())));
                rootList.put(project.getPcId(), jsonObject);
            }
        }
//        System.out.println(rootList);
        Collection<JsonObject> values = rootList.values();
        return values;
    }

    @Override
    public List<Project> getProjects(LoginUser userInfo) {
        // 查询该用户能看的所有项目
        List<Project> projects = null;
        if (userInfo.isSuper()) {
            projects = baseMapper.selectList(null);
        } else {
            Long userGroupId = userInfo.getUserGroupId();
            projects = baseMapper.selectListByGroupId(userGroupId);
        }
        return projects;
    }

    @Override
    public List<InnerProjectSearchDTO> extendList(QueryWrapper<Object> queryWrapper) {
        List<InnerProjectSearchDTO> innerProjectSearchDTOS = baseMapper.selectExtendList(queryWrapper);
        // 设置项目数据
        // 收集所有的 deliveryUnitIds
        Set<Long> deliveryUnitIds = innerProjectSearchDTOS.stream()
                .flatMap(innerProjectSearchDTO -> Optional.ofNullable(innerProjectSearchDTO.getDeliveryUnitIds())
                        .map(array -> array.toList(Long.class).stream())
                        .orElse(Stream.empty()))
                .collect(Collectors.toSet());
        if (!deliveryUnitIds.isEmpty()) {
            // 根据 deliveryUnitIds 查询 deliveryUnit 表
            Map<Long, DeliveryUnit> deliveryUnitMap = deliveryUnitService.listByIds(deliveryUnitIds).stream()
                    .collect(Collectors.toMap(DeliveryUnit::getId, deliveryUnit -> deliveryUnit));
            // 将 deliveryUnit 数据设置到每一个 InnerProjectSearchDTO 对象中
            for (InnerProjectSearchDTO item : innerProjectSearchDTOS) {
                JSONArray deliveryUnitIdsArray = item.getDeliveryUnitIds();
                if (deliveryUnitIdsArray == null) {
                    continue;
                }
                List<Long> deliveryUnitIdList = deliveryUnitIdsArray.toList(Long.class);
                List<DeliveryUnit> list = deliveryUnitIdList.stream().map(deliveryUnitId -> {
                    DeliveryUnit deliveryUnit = deliveryUnitMap.get(deliveryUnitId);
                    return deliveryUnit;
                }).toList();
                item.setDeliveryUnits(list);
            }
        }

        return innerProjectSearchDTOS;
    }

    @Override
    public Collection<JsonObject> getAllProjectsByUserGroup(LoginUser userInfo) {
        // 查询所有的项目
        List<ProjectDTO> projects = baseMapper.selectAllProjects();
        List<ProjectClassification> projectClassificationList = projectClassificationMapper.selectList(null);
        return buildTree(userInfo.getUserGroupId(), projectClassificationList, projects);
    }

}
