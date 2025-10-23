package com.oriseq.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.PackageGroupDTO;
import com.oriseq.dtm.dto.PackageManageInfoDTO;
import com.oriseq.dtm.entity.Package;
import com.oriseq.dtm.entity.Project;
import com.oriseq.dtm.vo.PackageVO;
import com.oriseq.dtm.vo.packageInfo.PackageSearchVO;
import com.oriseq.mapper.PackageMapper;
import com.oriseq.mapper.ProjectMapper;
import com.oriseq.service.IPackageService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:37
 */
@Service
public class PackageServiceImpl extends ServiceImpl<PackageMapper, Package> implements IPackageService {

    @Autowired
    private ProjectMapper projectMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<PackageVO> getPackageInfo(LoginUser userInfo) {
        // 查询套餐信息
        Long userGroupId = userInfo.getUserGroupId();
        /*
        - 通用套餐（用户组id==0）
        - 特殊套餐（特定用户组id）
        - 上次提交的套餐（用户id）
         */
        LambdaQueryWrapper<Package> qw = new LambdaQueryWrapper<Package>();
        if (!userInfo.isSuper()) {
            qw.eq(Package::getUserGroupId, userGroupId)
                    .or().eq(Package::getUserGroupId, 0)
                    .or().eq(Package::getUserId, userInfo.getUserId());
        } else {
            qw.ne(Package::getUserGroupId, "")
                    .or().eq(Package::getUserId, userInfo.getUserId());
        }
        List<Package> packages =
                this.baseMapper.selectList(qw);
        if (!packages.isEmpty()) {
            List<PackageVO> packageVOS = new ArrayList<>();
            // 查询这个用户能看到的项目信息
            // 超级管理员看到所有
            List<String> authList = null;
            if (userInfo.isSuper() || userInfo.isInternalGroup()) {
                // 查询所有ids
                List<Project> projects = projectMapper.selectList(null);
                authList = projects.stream().map(project -> project.getId().toString()).collect(Collectors.toList());
            } else {
                authList = projectMapper.selectAllProjectIdsByUserGroupId(userGroupId);
            }
            if (authList != null) {
                for (Package subPackage : packages) {
                    PackageVO packageVO = new PackageVO();
                    packageVO.setId(subPackage.getId());
                    packageVO.setPackageName(subPackage.getPackageInfo());
                    String projectIdList = subPackage.getProjectIdList();
                    if (StringUtils.isNotBlank(projectIdList)) {
                        String[] split = projectIdList.split(",");
                        List<String> list = Arrays.asList(split);
                        // 存在authList的才查询
                        list = list.stream().filter(authList::contains).collect(Collectors.toList());
                        if (!list.isEmpty()) {
                            // 查询项目信息
                            List<Project> projects = projectMapper.selectBatchIds(list);
                            // 排序让orderedProjects依据list的顺序
                            Map<String, Project> projectMap = projects.stream()
                                    .collect(Collectors.toMap(project -> project.getId().toString(), project -> project));
                            List<Project> orderedProjects = list.stream()
                                    .map(projectId -> projectMap.get(projectId))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList());
                            List<PackageVO.Projects> collect = orderedProjects.stream().map(project -> {
                                PackageVO.Projects proj = new PackageVO.Projects();
                                BeanUtils.copyProperties(project, proj);
                                // 默认 2：确认检测
                                proj.setStatus(2);
                                return proj;
                            }).collect(Collectors.toList());
                            packageVO.setProjects(collect);
                        }
                    }
                    packageVOS.add(packageVO);
                }
                return packageVOS;
            }

        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Collection<JSONObject> getBelongingPackageCascader(LoginUser userInfo) {
        // {group_name=序源, project_id_list=1,2,3, id=1, package_info=测试1, user_group_id=1}
        List<PackageGroupDTO> packageGroupDTOS = baseMapper.selectBelongingPackage();
        // 非超级管理员，过滤为同一个组
        if (!userInfo.isSuper()) {
            packageGroupDTOS = packageGroupDTOS.stream().filter(item ->
                    Objects.equals(item.getUserGroupId(), userInfo.getUserGroupId())).collect(Collectors.toList());
        }
        // 构建树
        HashMap<Long, JSONObject> root = new HashMap<>();
        for (PackageGroupDTO packageGroupDTO : packageGroupDTOS) {
            if (root.getOrDefault(packageGroupDTO.getUserGroupId(), null) != null) {
                JSONObject entries = root.get(packageGroupDTO.getUserGroupId());
                JSONArray children = entries.getJSONArray("children");
                JSONObject entries2 = new JSONObject();
                entries2.set("id", packageGroupDTO.getPackageId());
                entries2.set("value", packageGroupDTO.getPackageId());
                entries2.set("label", packageGroupDTO.getPackageName());
                children.add(entries2);
            } else {
                // 初始进入
                JSONObject entries = new JSONObject();
                entries.set("id", packageGroupDTO.getUserGroupId());
                entries.set("value", packageGroupDTO.getUserGroupId());
                entries.set("label", packageGroupDTO.getGroupName());
                JSONArray objects = new JSONArray();
                entries.set("children", objects);
                root.put(packageGroupDTO.getUserGroupId(), entries);
                if (packageGroupDTO.getPackageId() != null) {
                    JSONObject entries2 = new JSONObject();
                    entries2.set("id", packageGroupDTO.getPackageId());
                    entries2.set("value", packageGroupDTO.getPackageId());
                    entries2.set("label", packageGroupDTO.getPackageName());
                    objects.add(entries2);
                }
            }
        }
        return root.values();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<PackageManageInfoDTO> getPackageManageInfo(LoginUser userInfo, PackageSearchVO packageSearchVO) {
        QueryWrapper<Object> queryWp = new QueryWrapper<>();
        // 除去用户自己的上次提交
        queryWp.isNotNull("user_group_id");
        if (!userInfo.isSuper()) {
            Long userGroupId = userInfo.getUserGroupId();
            queryWp.eq("user_group_id", userGroupId);
        }
        // 查询条件
        if (StringUtils.isNotBlank(packageSearchVO.getPackageName())) {
            queryWp.like("package_info", packageSearchVO.getPackageName());
        }
        if (StringUtils.isNotBlank(packageSearchVO.getUserGroupId())) {
            queryWp.eq("user_group_id", packageSearchVO.getUserGroupId());
        }
        List<PackageManageInfoDTO> packageManageInfoDTOS = baseMapper.selectPackageMangInfo(queryWp);
        // 查询条件
        if (packageSearchVO.getProjectIds() != null) {
            packageManageInfoDTOS = packageManageInfoDTOS.stream().filter(item -> {
                String projectIdList = item.getProjectIdList();
                List<String> packageProjectIdList = Arrays.stream(projectIdList.split(",")).toList();
                List<Long> projectIds = packageSearchVO.getProjectIds();
                // 转成String类型
                List<String> projectIdsStr = projectIds.stream().map(String::valueOf).toList();
                boolean b = packageProjectIdList.containsAll(projectIdsStr);
                return b;
            }).toList();
        }

        // 获取项目信息
        // 1. 收集所有的packageManageInfoDTOS中的ProjectIds并去重
        Set<String> allProjectIds = packageManageInfoDTOS.stream()
                .map(PackageManageInfoDTO::getProjectIdList)
                .flatMap(projectIdList -> Arrays.stream(projectIdList.split(",")))
                .collect(Collectors.toSet());

        // 2. 根据去重后的projectIds查询项目信息
        List<Project> projects = new ArrayList<>();
        if (!allProjectIds.isEmpty()) {
            projects = projectMapper.selectBatchIds(allProjectIds);
        }

        // 3. 将查询到的项目信息设置到packageManageInfoDTOS的每一个元素中
        Map<String, Project> projectMap = projects.stream()
                .collect(Collectors.toMap(project -> project.getId().toString(), project -> project));

        for (PackageManageInfoDTO packageManageInfoDTO : packageManageInfoDTOS) {
            String projectIdList = packageManageInfoDTO.getProjectIdList();
            List<String> packageProjectIdList = Arrays.stream(projectIdList.split(",")).toList();
            List<PackageManageInfoDTO.Project> packageProjects = packageProjectIdList.stream()
                    .map(projectMap::get)
                    .filter(Objects::nonNull)
                    .map(project -> {
                        PackageManageInfoDTO.Project projectInPackage = new PackageManageInfoDTO.Project();
                        projectInPackage.setId(project.getId());
                        projectInPackage.setProjectName(project.getProjectName());
                        return projectInPackage;
                    })
                    .collect(Collectors.toList());
            packageManageInfoDTO.setProjects(packageProjects);
        }

        return packageManageInfoDTOS;
    }
}
