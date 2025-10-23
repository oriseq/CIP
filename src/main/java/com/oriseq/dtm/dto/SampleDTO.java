package com.oriseq.dtm.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description: 对应查询语句
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/3 16:26
 */
@Data
public class SampleDTO {
    private Long id;
    private String name;
    private Integer sex;
    private Integer age;
    private LocalDate birthday;
    private Integer isBirthInput;
    private Integer status;
    private Long userId;
    private Long userGroupId;
    private String annexFileId;
    private LocalDateTime submissionTime;
    private LocalDateTime updateTime;
    /**
     * 物流单号
     */
    private String logisticsTrackingNumber;

    /**
     * 手机号后四位
     */
    private String phoneNumLastFour;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 报告id(文件)
     */
    private String reportId;

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
     * 样本下的项目唯一编号
     */
    private Long sampleProjectId;
    private Long projectId;
    private Integer projectStatus;
    private String projectName;

    /**
     * 当天提交编号
     */
    private String submitNumberToday;

    /**
     * 删除状态 0:未删除，1:已删除
     */
    private Boolean isDel;
}
