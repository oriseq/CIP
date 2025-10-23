package com.oriseq.dtm.vo.sample;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oriseq.dtm.vo.SampleBasicInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ExportProjectSampleVO {

    /**
     * 送检人姓名
     */
    private String name;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 当天提交编号
     */
    private String submitNumberToday;
    /**
     * 样本的id
     */
    private Long id;
    private LocalDate birthday;
    private Integer status;
    private String annexFileId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submissionTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    private List<SampleProjectVO> projects = new ArrayList<>();
    private JSONObject fileInfo;
    /**
     * 代管用户
     */
    private List<String> managedUsers;
    /**
     * 物流单号
     */
    private String logisticsTrackingNumber;
    /**
     * 手机号后四位
     */
    private String phoneNumLastFour;
    /**
     * 1（代管），2（被代管），3（普通）
     */
    private String sampleHostingStatus;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 样本编号
     */
    private String sampleId;
    /**
     * 样本归属用户组ID
     */
    private Long sampleUserGroupId;
    /**
     * 样本归属用户组名称
     */
    private String sampleUserGroupName;
    /**
     * 项目状态统计
     */
    private List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatistics;
    /**
     * 删除状态 0:未删除，1:已删除
     */
    private Boolean isDel;

    public ExportProjectSampleVO(SampleInfoVO sampleInfoVO, SampleProjectVO project) {
//        this.sampleId = sampleInfoVO.getSampleId();
//        this.sampleSubmissionTime = sampleInfoVO.getSubmissionTime();
//        this.isDeliveryOutside = project.getIsDeliveryOutside() ? "是" : "否";
//        this.projectStatus = project.getProjectStatus();
//        this.creationTime = project.getCreationTime();
//        this.deliveryUnit = project.getDeliveryUnit();
        this.name = sampleInfoVO.getName();
        this.sex = sampleInfoVO.getSex();
        this.age = sampleInfoVO.getAge();
        this.projectName = project.getProjectName();
        this.projects = sampleInfoVO.getProjects();
        this.submitNumberToday = sampleInfoVO.getSubmitNumberToday();
        this.id = sampleInfoVO.getId();
        this.birthday = sampleInfoVO.getBirthday();
        this.status = sampleInfoVO.getStatus();
        this.annexFileId = sampleInfoVO.getAnnexFileId();
        this.submissionTime = sampleInfoVO.getSubmissionTime();
        this.updateTime = sampleInfoVO.getUpdateTime();
        this.fileInfo = sampleInfoVO.getFileInfo();
        this.managedUsers = sampleInfoVO.getManagedUsers();
        this.logisticsTrackingNumber = sampleInfoVO.getLogisticsTrackingNumber();
        this.phoneNumLastFour = sampleInfoVO.getPhoneNumLastFour();
        this.sampleHostingStatus = sampleInfoVO.getSampleHostingStatus();
        this.remarks = sampleInfoVO.getRemarks();
        this.sampleId = sampleInfoVO.getSampleId();
        this.sampleUserGroupId = sampleInfoVO.getSampleUserGroupId();
        this.sampleUserGroupName = sampleInfoVO.getSampleUserGroupName();
        this.projectStatusStatistics = sampleInfoVO.getProjectStatusStatistics();
        this.isDel = sampleInfoVO.getIsDel();
    }
}
