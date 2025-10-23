package com.oriseq.dtm.vo.sample;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oriseq.dtm.vo.SampleBasicInfo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SampleInfoVO {
    private Long id;
    private String name;
    private Integer sex;
    private Integer age;
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
     * 项目状态统计
     */
    private List<SampleBasicInfo.ProjectStatusStatistics> projectStatusStatistics;

    /**
     * 当天提交编号
     */
    private String submitNumberToday;

    /**
     * 删除状态 0:未删除，1:已删除
     */
    private Boolean isDel;

}