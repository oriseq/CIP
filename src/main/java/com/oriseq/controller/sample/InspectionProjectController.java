package com.oriseq.controller.sample;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oriseq.common.log.EnableLogging;
import com.oriseq.common.utils.ListStringConverter;
import com.oriseq.config.LoginTool;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectManageDTO;
import com.oriseq.dtm.entity.*;
import com.oriseq.dtm.enums.ProjectStatus;
import com.oriseq.dtm.vo.*;
import com.oriseq.dtm.vo.project.ProjectReviewVO;
import com.oriseq.dtm.vo.project.UpdateProjectStatusVO;
import com.oriseq.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 样本项目管理
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/30 11:19
 */
@Slf4j
@RestController
@RequestMapping("sampleProject")
@EnableLogging
public class InspectionProjectController {


    @Autowired
    private LoginTool loginTool;

    @Autowired
    private ISampleService sampleService;

    @Autowired
    private ISampleHostingService sampleHostingService;

    @Autowired
    private ISampleProjectService sampleProjectService;

    @Autowired
    private IFileInfoService fileInfoService;

    @Autowired
    private ResultTypeService resultTypeService;

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IProjectService projectService;


    /**
     * 获取样本信息-送检人信息
     *
     * @return
     */
    @GetMapping("sampleInfo/{id}")
    public Result<SampleBasicInfo> sampleInformation(
            @PathVariable("id") Long id,
            HttpServletRequest request) {
//        LoginUser user = loginTool.getUserInfo(request);
        Sample byId = sampleService.getById(id);
        SampleBasicInfo sampleBasicInfo = new SampleBasicInfo();
        BeanUtils.copyProperties(byId, sampleBasicInfo);
        List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatisticsList = sampleProjectService.projectStatusStatistics(id);
        sampleBasicInfo.setProjectStatusStatistics(projectStatusStatisticsList);
        return Result.defaultSuccessByMessageAndData("获取成功", sampleBasicInfo);
    }

    /**
     * 获取送检样本信息
     *
     * @return
     */
    @PostMapping("sampleProjectInfo")
    public Result<List> sampleInformation(
            @Validated @RequestBody(required = false) ProjectsRequestVO projectsRequestVO) {
        LoginUser userInfo = loginTool.getUserInfo();
        List<ProjectManageDTO> projectManageDTOS = sampleProjectService.listProject(projectsRequestVO);
        // 过滤项目, 搜索条件
        List<Long> projects = projectsRequestVO.getProjects();
        if (projects != null && !projects.isEmpty()) {
            projectManageDTOS = projectManageDTOS.stream().filter(projectManageDTO -> projects.contains(projectManageDTO.getProjectId())).toList();
        }
        // 不是超级管理员&不是内部组，过滤掉字段
        if (!userInfo.isSuper() && !userInfo.getIsInternalGroup()) {
            List<JSONObject> jsonObjectList = projectManageDTOS.stream().map(projectManageDTO -> {
                projectManageDTO.setDeliveryUnit(null);
                projectManageDTO.setDeliveryPerson(null);
                projectManageDTO.setResultImg(null);
                projectManageDTO.setReviewResults(null);
                JSONObject entries = JSONUtil.parseObj(projectManageDTO);
                return entries;
            }).toList();
            return Result.defaultSuccessByMessageAndData("获取成功", jsonObjectList);
        }
        return Result.defaultSuccessByMessageAndData("获取成功", projectManageDTOS);
    }

    /**
     * 是否代管用户-对于这个样本
     *
     * @return
     */
    @GetMapping("isEscrowSample/{id}")
    public Result<Boolean> isEscrowSample(
            @PathVariable("id") Long sampleId) {
        LoginUser userInfo = loginTool.getUserInfo();
        SampleHosting one = sampleHostingService.getOne(new LambdaQueryWrapper<SampleHosting>()
                .eq(SampleHosting::getSampleId, sampleId)
                .eq(SampleHosting::getHostingUserId, userInfo.getUserId()));
        if (one != null) {
            return Result.defaultSuccessByMessageAndData("获取成功", true);
        }
        return Result.defaultSuccessByMessageAndData("获取成功", false);
    }


    /**
     * 新增项目
     *
     * @return
     */
    @PostMapping("sampleProjects")
    public Result updateProjects(
            @Validated @RequestBody(required = false) UpdateProjectsVO updateProjectsVO,
            HttpServletRequest request) {
//        LoginUser user = loginTool.getUserInfo(request);
        List<UpdateProjectsVO.Project> projects = updateProjectsVO.getProjects();
        List<SampleProject> sampleProjects = projects.stream().map(project -> {
            SampleProject sampleProject = new SampleProject();
            sampleProject.setSampleId(updateProjectsVO.getId());
            sampleProject.setProjectId(project.getId());
            sampleProject.setProjectStatus(project.getStatus());
            // 计算截止时间
            LocalDateTime deadline = sampleProjectService.calculateTheDeadline(project.getId(), LocalDateTime.now());
            sampleProject.setDeadline(deadline);
            return sampleProject;
        }).toList();
        sampleProjectService.saveOrUpdateBatch(sampleProjects);
        // 更新样本状态
        sampleService.updateSampleStatus(updateProjectsVO.getId());
        return Result.defaultSuccessByMessage("新增成功");
    }

    /**
     * 删除项目
     *
     * @return
     */
    @PutMapping("sampleProjectInfo")
    public Result<ArrayList<String>> deleteSampleInformation(@Validated @RequestBody DeleteProjectVO deleteSampleVO, HttpServletRequest request) {
        // 修改项目状态
        sampleProjectService.changeProjects(deleteSampleVO);
        return Result.defaultSuccessByMessage("删除成功");
    }

    /**
     * 修改项目
     *
     * @return
     */
    @PutMapping("sampleProject")
    public Result<ArrayList<String>> updateProjectStatus(@Validated @RequestBody @NotNull UpdateProjectStatusVO updateProjectStatusVO) {
        LoginUser user = loginTool.getUserInfo();
        // 当前修改的样本项目信息
        SampleProject one = sampleProjectService.getOne(new LambdaQueryWrapper<SampleProject>().eq(SampleProject::getSampleId, updateProjectStatusVO.getSampleId())
                .eq(SampleProject::getProjectId, updateProjectStatusVO.getProjectId()));
        Integer needingStatus = updateProjectStatusVO.getProjectStatus();
        // 取消可操作度的限制
        // 状态不一样就会触发
//        if (!needingStatus.equals(one.getProjectStatus())) {
//            boolean allow = this.canUpdateStatus(needingStatus, one, user);
//            if (!allow) {
//                return Result.defaultErrorByMessage("因项目状态，无法执行这个操作");
//            }
//        }
        // 其他字段
        BeanUtils.copyProperties(updateProjectStatusVO, one);
        // 2. 执行更新
        one.setProjectStatus(needingStatus);
        // 处理resultImgList
        if (CollUtil.isNotEmpty(updateProjectStatusVO.getResultImgList())) {
            String resultImg = ListStringConverter.listToString(updateProjectStatusVO.getResultImgList());
            one.setResultImg(resultImg);
        }
        sampleProjectService.updateById(one);
        // 更新样本状态
        sampleService.updateSampleStatus(one.getSampleId());
        return Result.defaultSuccessByMessage("修改成功");

    }

    /**
     * // 1. 判断可操作度
     * <p>
     * * 普通用户：
     * 1. 判断属于<待定>和<确认检测>时才能操作，能修改成<待定>、<确认检测>、<取消>
     * 1. 待定
     * - 取消
     * - 确认检测
     * 2. 确认检测
     * - 取消
     * - 待定
     * <p>
     * 超级管理：
     * <p>
     * 2. 判断属于<待定>和<确认检测>时才能操作
     * 1. 待定
     * - 取消
     * - 确认检测
     * 2. 确认检测
     * - 取消
     * - 正在检测
     *
     * @param needingStatus
     * @param one
     * @param user
     * @return
     */
    private boolean canUpdateStatus(Integer needingStatus, SampleProject one, LoginUser user) {
        Integer originStatus = one.getProjectStatus();
        // 允许操作
        boolean allow = false;
        if (loginTool.isSuper(user)) {
            if (originStatus == ProjectStatus.PENDING.getValue()) {
                if (needingStatus == ProjectStatus.CANCELLED.getValue()
                        || needingStatus == ProjectStatus.CONFIRMING.getValue()) {
                    allow = true;
                }
            } else if (originStatus == ProjectStatus.CONFIRMING.getValue()) {
                if (needingStatus == ProjectStatus.CANCELLED.getValue()
                        || needingStatus == ProjectStatus.IN_PROGRESS.getValue()) {
                    allow = true;
                }
            }
        } else {
            if (originStatus == ProjectStatus.PENDING.getValue()) {
                if (needingStatus == ProjectStatus.CANCELLED.getValue()
                        || needingStatus == ProjectStatus.CONFIRMING.getValue()) {
                    allow = true;
                }
            } else if (originStatus == ProjectStatus.CONFIRMING.getValue()) {
                if (needingStatus == ProjectStatus.CANCELLED.getValue()
                        || needingStatus == ProjectStatus.PENDING.getValue()) {
                    allow = true;
                }
            }
        }
        return allow;
    }


    /**
     * 上传报告数据
     *
     * @return
     */
    @PostMapping("uploadReportData")
    public Result<ArrayList<String>> uploadReportData(@Validated @RequestBody UploadReportVO uploadReportVO, HttpServletRequest request) {
        SampleProject one = sampleProjectService.getOne(new LambdaQueryWrapper<SampleProject>().eq(SampleProject::getProjectId, uploadReportVO.getProjectId())
                .eq(SampleProject::getSampleId, uploadReportVO.getSampleId()));
        // 判断是不是正在检测状态或已完成状态或确认检测状态
        if (one == null) {
            return Result.defaultErrorByMessage("项目不存在");
        }
        if (!(one.getProjectStatus() == ProjectStatus.IN_PROGRESS.getValue()
                || one.getProjectStatus() == ProjectStatus.COMPLETED.getValue()
                || one.getProjectStatus() == ProjectStatus.CONFIRMING.getValue())) {
            return Result.defaultErrorByMessage("当前项目状态不属于【正在检测、确认检测、已完成】状态其中之一");
        }
        sampleProjectService.uploadReportData(uploadReportVO, one);

        return Result.defaultSuccessByMessage("上传成功");
    }

    /**
     * 项目复核
     *
     * @return
     */
    @PostMapping("review")
    public Result<ArrayList<String>> sampleHosting(@Validated @RequestBody ProjectReviewVO projectReviewVO) {
        for (Long projectId : projectReviewVO.getProjectIds()) {
            SampleProject sampleProject = new SampleProject();
            sampleProject.setReviewResults(projectReviewVO.getReviewResults());
            sampleProjectService.update(sampleProject, new LambdaQueryWrapper<SampleProject>().eq(SampleProject::getSampleId, projectReviewVO.getSampleId())
                    .eq(SampleProject::getProjectId, projectId));
        }
        return Result.defaultSuccessByMessage("操作成功");
    }

    /**
     * 用于选择器的报告结果
     *
     * @return
     */
    @GetMapping("reportResultsSelect")
    public Result<List<String>> reportResultsSelect() {
        LambdaQueryWrapper<ResultType> resultTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ResultType> select = resultTypeLambdaQueryWrapper.select(ResultType::getText);
        List<ResultType> list = resultTypeService.list(select);
        List<String> list1 = list.stream().map(ResultType::getText).toList();
        return Result.defaultSuccessByMessageAndData("查询成功", list1);
    }

    /**
     * 外送人选择器
     */
    @GetMapping("deliveryPersonSelect")
    public Result<List<String>> deliveryPersonSelect() {
        // 获取所有属于内部组的用户,以返回真实姓名的List
        List<User> innerGroupUserList = usersService.getInnerGroupUserList();
        // 如果没有真实姓名，那么返回用户名，没有用户名返回手机号
        List<String> list1 = innerGroupUserList.stream().map(user -> {
            String displayName = user.getRealName();
            if (StringUtils.hasText(displayName)) {
                return displayName;
            }
            displayName = user.getUsername();
            if (StringUtils.hasText(displayName)) {
                return displayName;
            }
            return user.getPhoneNumber();
        }).toList();
        return Result.defaultSuccessByMessageAndData("查询成功", list1);
    }

}
