package com.oriseq.dtm.entity;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author huang
 * @date 2024/05/21
 */
@Data
@TableName(value = "project_master", autoResultMap = true)
public class Project {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String projectName;
    //    private Integer isSingleItem;
    private String detectMethod;
    private String consumableType;
    private Long projectCategoryId;
    /**
     * 价格
     */
    private BigDecimal price;


    /**
     * 样本要求
     */
    private String sampleRequirements;
    /**
     * 时分刻度是否置末(1:是，0:否)
     */
    private Boolean isReportHourMinutesEnd;

    /**
     * 报告周期（文字记录）
     */
    private String reportDate;
    /**
     * 报告天数
     */
    private Integer reportDay;
    /**
     * 报告小时
     */
    private Integer reportHour;
    /**
     * 排除星期：[1,2,3,4,5,6,7]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray disableWeek;
    /**
     * 警报剩余时间（小时）
     */
    private String remainingHour;
    /**
     * 备注
     */
    private String remarks;


    /**
     * 包含的外送单位ids
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray deliveryUnitIds;

    /**
     * 仪器id
     */
    private Long instrumentId;

    /**
     * 关联的 sample_project 表
     */
    @TableField(exist = false)
    private Long sampleId;

    /**
     * 关联的 sample_project 表
     */
    @TableField(exist = false)
    private Integer projectStatus;

    /**
     * 关联的 sample_project 表 的id
     */
    @TableField(exist = false)
    private Long sampleProjectId;

    /**
     * 是否外送
     */
    @TableField(exist = false)
    private Boolean isDeliveryOutside;

    /**
     * 关联的 sample_project 表 的提交时间
     */
    @TableField(exist = false)
    private LocalDateTime creationTime;

    /**
     * 关联的 sample_project 表 的外送单位
     */
    @TableField(exist = false)
    private String deliveryUnit;

}