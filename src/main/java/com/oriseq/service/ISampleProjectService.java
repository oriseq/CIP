package com.oriseq.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.NeedAlarmSampleProjectDTO;
import com.oriseq.dtm.dto.ProjectManageDTO;
import com.oriseq.dtm.entity.SampleProject;
import com.oriseq.dtm.vo.DeleteProjectVO;
import com.oriseq.dtm.vo.ProjectsRequestVO;
import com.oriseq.dtm.vo.SampleBasicInfo;
import com.oriseq.dtm.vo.UploadReportVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 14:16
 */
public interface ISampleProjectService extends IService<SampleProject> {
    /**
     * 查询项目列表
     *
     * @param projectsRequestVO
     * @return
     */
    List<ProjectManageDTO> listProject(ProjectsRequestVO projectsRequestVO);

    /**
     * 修改项目状态、删除
     *
     * @param deleteSampleVO
     */
    void changeProjects(DeleteProjectVO deleteSampleVO);

    /**
     * 如果所有项目报告已完成后发送通知
     *
     * @param sampleId
     */
    void allCompletedAndSendNotification(Long sampleId);

    /**
     * 计算项目截止时间
     *
     * @param projectId 项目id
     * @param start     开始时间
     * @return
     */
    LocalDateTime calculateTheDeadline(Long projectId, LocalDateTime start);

    List<NeedAlarmSampleProjectDTO> getNeedAlarmSampleProject();

    List<ProjectManageDTO> sampleProjects(List<Long> sampleIds, String scene);

    /**
     * 项目状态统计
     *
     * @return
     */
    List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatistics(Long sampleId);

    void uploadReportData(UploadReportVO uploadReportVO, SampleProject one);

    List<ProjectManageDTO> listProjectList(QueryWrapper<Object> in);
}
