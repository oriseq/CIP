package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@TableName("sample")
public class Sample {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer sex;
    private Integer age;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty
    private LocalDate birthday;
    private Integer isBirthInput;
    private Integer status;
    private Long userId;
    private Long userGroupId;
    private String annexFileId;
    private LocalDateTime submissionTime;
    @TableField(fill = FieldFill.UPDATE)
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
     * 样本编号（业务）
     */
    @TableField("sample_id")
    private String sampleId;

    /**
     * 样本归属用户组ID/送检单位（业务）
     */
    private Long sampleUserGroupId;

    /**
     * 当天提交编号
     */
    private String submitNumberToday;

    /**
     * 删除状态 0:未删除，1:已删除
     */
    private Boolean isDel;

    /**
     * 关联的 project 表
     */
    @TableField(exist = false)
    private List<Project> projects;


    /**
     * 查询包含的项目数量
     */
    @TableField(exist = false)
    private Integer projectCount;

    /**
     * 关联的 user_group 表
     */
    @TableField(exist = false)
    private String sampleUserGroupName;

}