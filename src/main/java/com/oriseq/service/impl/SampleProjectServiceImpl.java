package com.oriseq.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.common.utils.ListStringConverter;
import com.oriseq.dtm.dto.NeedAlarmSampleProjectDTO;
import com.oriseq.dtm.dto.ProjectManageDTO;
import com.oriseq.dtm.entity.Project;
import com.oriseq.dtm.entity.Sample;
import com.oriseq.dtm.entity.SampleHosting;
import com.oriseq.dtm.entity.SampleProject;
import com.oriseq.dtm.enums.ProjectStatus;
import com.oriseq.dtm.vo.DeleteProjectVO;
import com.oriseq.dtm.vo.ProjectsRequestVO;
import com.oriseq.dtm.vo.SampleBasicInfo;
import com.oriseq.dtm.vo.UploadReportVO;
import com.oriseq.mapper.ProjectMapper;
import com.oriseq.mapper.SampleHostingMapper;
import com.oriseq.mapper.SampleMapper;
import com.oriseq.mapper.SampleProjectMapper;
import com.oriseq.service.IFileInfoService;
import com.oriseq.service.ISampleProjectService;
import com.oriseq.service.ISampleService;
import com.oriseq.service.MessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/22 9:46
 */
@Service
public class SampleProjectServiceImpl extends ServiceImpl<SampleProjectMapper, SampleProject> implements ISampleProjectService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleHostingMapper sampleHostingMapper;

    @Autowired
    private MessageNotificationService messageNotificationService;

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    @Lazy
    private ISampleService sampleService;
    @Autowired
    private IFileInfoService fileInfoService;


    @Override
    public List<ProjectManageDTO> listProject(ProjectsRequestVO projectsRequestVO) {
        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("sample_id", projectsRequestVO.getId());
        objectQueryWrapper.eq(projectsRequestVO.getStatus() != null, "project_status", projectsRequestVO.getStatus());
        objectQueryWrapper.like(StrUtil.isNotBlank(projectsRequestVO.getRemarks()), "remarks", projectsRequestVO.getRemarks());
        // 查询未删除数据
        objectQueryWrapper.eq("is_del", 0);
        objectQueryWrapper.last("ORDER BY ABS(DATEDIFF(sp.deadline, CURDATE())) ASC");
        List<ProjectManageDTO> projectManageDTOS = baseMapper.selectProjects(objectQueryWrapper);
        return projectManageDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeProjects(DeleteProjectVO deleteSampleVO) {
        List<String> ids = deleteSampleVO.getIds();
        // 1.查所有项目
        List<SampleProject> sampleProjects = baseMapper.selectList(new LambdaQueryWrapper<SampleProject>().in(SampleProject::getProjectId, ids)
                .eq(SampleProject::getSampleId, deleteSampleVO.getSampleId()));
        // 2.判断能否删除
        List<SampleProject> notSuitList = sampleProjects.stream().filter(sampleProject -> sampleProject.getProjectStatus() != ProjectStatus.CANCELLED.getValue()
                && sampleProject.getProjectStatus() != ProjectStatus.PENDING.getValue()).toList();
        if (notSuitList.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("删除失败，仅能删除状态为待定的项目。");
            throw new RuntimeException(stringBuilder.toString());
        }
        // 3.删除项目
//        List<SampleProject> list = sampleProjects.stream().map(sampleProject -> {
//            sampleProject.setIsDel(true);
//            return sampleProject;
//        }).toList();
//        this.updateBatchById(list);
        List<Long> list = sampleProjects.stream().map(SampleProject::getId).toList();
        baseMapper.deleteBatchIds(list);
        // 更新样本状态
        sampleService.updateSampleStatus(deleteSampleVO.getSampleId());
    }

    @Override
    public void allCompletedAndSendNotification(Long sampleId) {
        LambdaQueryWrapper<SampleProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SampleProject::getSampleId, sampleId);
        List<SampleProject> sampleProjects = baseMapper.selectList(wrapper);
        // 是否都有了报告文件
        boolean b = sampleProjects.stream().allMatch(sampleProject -> {
            String fileId = sampleProject.getFileId();
            return StrUtil.isNotBlank(fileId);
        });
        if (b) {
            // 发送通知
            // 获取用户
            // 1\送检任务提交者  2\代管该送检任务的人
            List<Long> allUserIds = new ArrayList<>();
            Sample byId = sampleMapper.selectById(sampleId);
            Long userId = byId.getUserId();
            allUserIds.add(userId);

            LambdaQueryWrapper<SampleHosting> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(SampleHosting::getSampleId, sampleId);
            List<SampleHosting> list = sampleHostingMapper.selectList(wrapper1);
            List<Long> userIds = list.stream().map(SampleHosting::getHostingUserId).toList();
            allUserIds.addAll(userIds);

            // 发送通知
            messageNotificationService.sendNotificationsWhenSampleReportFullyCompleted(allUserIds, sampleId);

        }
    }

    @Override
    public LocalDateTime calculateTheDeadline(Long projectId, LocalDateTime start) {
        Project project = projectMapper.selectById(projectId);
        Boolean isReportHourMinutesEnd = project.getIsReportHourMinutesEnd();
        if (isReportHourMinutesEnd == null) {
            throw new RuntimeException("时分刻度是否置末字段不能为null");
        }
        Integer reportDay = Optional.ofNullable(project.getReportDay()).orElse(0);
        Integer reportHour = Optional.ofNullable(project.getReportHour()).orElse(0);
        JSONArray disableWeek = project.getDisableWeek();
        List<DayOfWeek> excludedDays = new ArrayList<>();
        if (disableWeek != null && disableWeek.size() > 0) {
            excludedDays = disableWeek.stream().map(dayOfWeek -> DayOfWeek.of(Integer.valueOf(dayOfWeek.toString()))).toList();
        }

        // 处理天
        LocalDateTime deadline = start;
        int daysToAdd = 0;
        while (daysToAdd < reportDay) {
            deadline = deadline.plusDays(1); // 逐天递增
            if (!excludedDays.contains(deadline.getDayOfWeek())) {
                // 只计算非排除的星期
                daysToAdd++;
            }
        }

        // 处理时分
        if (isReportHourMinutesEnd) {
            // 把时分设置23.59分。
            deadline = deadline.withHour(23).withMinute(59);
        } else {
            // 按照当前时间延迟reportHour
            deadline = deadline.plusHours(reportHour);
        }
        return deadline;
    }

    @Override
    public List<NeedAlarmSampleProjectDTO> getNeedAlarmSampleProject() {
        return baseMapper.selectNeedAlarmSampleProject();
    }

    @Override
    public List<ProjectManageDTO> sampleProjects(List<Long> sampleIds, String scene) {
        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.in("sample_id", sampleIds);
        if (StrUtil.isNotBlank(scene) && scene.equals("uploadReports")) {
            objectQueryWrapper.ne("project_status", ProjectStatus.CANCELLED.getValue());
            objectQueryWrapper.ne("project_status", ProjectStatus.PENDING.getValue());
            objectQueryWrapper.ne("project_status", ProjectStatus.COMPLETED.getValue());
            objectQueryWrapper.isNull("file_id");
        }
        // 查询未删除数据
        objectQueryWrapper.eq("is_del", 0);
        List<ProjectManageDTO> projectManageDTOS = baseMapper.selectProjects(objectQueryWrapper);
        projectManageDTOS = projectManageDTOS.stream().sorted(Comparator.comparingInt(jObject -> {
            // 待定、确认检测、正在检测、完成、取消的顺序展示
            Integer status = jObject.getProjectStatus();
            // 待定、确认检测、正在检测排前面
            if (status == 1) {
                return 0;
            } else if (status == 2) {
                return 1;
            } else if (status == 3) {
                return 2;
            } else if (status == 4) {
                // 已完成排中间
                return 3;
            } else {
                // 取消排最后
                return 4;
            }
        })).toList();
        return projectManageDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatistics(Long sampleId) {
        // 统计项目 状态：数量
        List<SampleProject> list = baseMapper.selectList(new LambdaQueryWrapper<SampleProject>()
                .eq(SampleProject::getSampleId, sampleId));
        // 构建返回数据
        HashMap<Integer, Integer> numberMap = new HashMap<>();
        numberMap.put(ProjectStatus.CANCELLED.getValue(), 0);
        numberMap.put(ProjectStatus.PENDING.getValue(), 0);
        numberMap.put(ProjectStatus.CONFIRMING.getValue(), 0);
        numberMap.put(ProjectStatus.IN_PROGRESS.getValue(), 0);
        numberMap.put(ProjectStatus.COMPLETED.getValue(), 0);
        for (SampleProject sampleProject : list) {
            if (sampleProject.getProjectStatus().equals(ProjectStatus.CANCELLED.getValue())) {
                numberMap.put(ProjectStatus.CANCELLED.getValue(), numberMap.get(ProjectStatus.CANCELLED.getValue()) + 1);
            } else if (sampleProject.getProjectStatus().equals(ProjectStatus.PENDING.getValue())) {
                numberMap.put(ProjectStatus.PENDING.getValue(), numberMap.get(ProjectStatus.PENDING.getValue()) + 1);
            } else if (sampleProject.getProjectStatus().equals(ProjectStatus.CONFIRMING.getValue())) {
                numberMap.put(ProjectStatus.CONFIRMING.getValue(), numberMap.get(ProjectStatus.CONFIRMING.getValue()) + 1);
            } else if (sampleProject.getProjectStatus().equals(ProjectStatus.IN_PROGRESS.getValue())) {
                numberMap.put(ProjectStatus.IN_PROGRESS.getValue(), numberMap.get(ProjectStatus.IN_PROGRESS.getValue()) + 1);
            } else if (sampleProject.getProjectStatus().equals(ProjectStatus.COMPLETED.getValue())) {
                numberMap.put(ProjectStatus.COMPLETED.getValue(), numberMap.get(ProjectStatus.COMPLETED.getValue()) + 1);
            }
        }
        List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatisticsList = numberMap.entrySet().stream().map(entry -> {
            return new SampleBasicInfo.ProjectStatusStatistics(entry.getKey(), entry.getValue().intValue());
        }).toList();

        return projectStatusStatisticsList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void uploadReportData(UploadReportVO uploadReportVO, SampleProject one) {
        // 原本的报告处理
        String fileId = one.getFileId();
        if (StrUtil.isNotBlank(fileId)) {
            // 1.先判断是否被多个报告引用
            List<SampleProject> sampleProjects = baseMapper.selectList(new LambdaQueryWrapper<SampleProject>()
                    .eq(SampleProject::getFileId, fileId).eq(SampleProject::getSampleId, one.getSampleId()));
            if (sampleProjects.size() == 1) {
                // 存在一个报告引用，就需要直接删除。
                // 存在就先删除，删除数据文件
                fileInfoService.deleteReportFile(one.getSampleId(), fileId);
            }

        }
        // 设置新的报告文件id
        one.setFileId(uploadReportVO.getFileId());
        one.setPolarity(uploadReportVO.getPolarity());
        one.setProjectStatus(ProjectStatus.COMPLETED.getValue());
        this.updateById(one);
        // 更新文件所属样本状态
        fileInfoService.updateBelongSampleId(uploadReportVO.getFileId(), one.getSampleId());
        // 更新报告文件
        Sample byId = sampleService.getById(one.getSampleId());
        String reportId = byId.getReportId();
        List<String> strings = ListStringConverter.stringToList(reportId);
        strings.add(uploadReportVO.getFileId());
        byId.setReportId(ListStringConverter.listToString(strings));
        sampleService.updateById(byId);
        // 样本状态更新
        sampleService.updateSampleStatus(one.getSampleId());
        // 如果所有项目报告已完成后发送通知
        this.allCompletedAndSendNotification(one.getSampleId());
    }

    @Override
    public List<ProjectManageDTO> listProjectList(QueryWrapper<Object> in) {
        List<ProjectManageDTO> projectManageDTOS = baseMapper.selectProjects(in);
        return projectManageDTOS;
    }
}
