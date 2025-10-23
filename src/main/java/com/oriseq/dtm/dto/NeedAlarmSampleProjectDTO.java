package com.oriseq.dtm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 需要报警的样本项目数据传输对象 (DTO)
 * 用于在不同层之间传递需要报警的样本项目数据。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeedAlarmSampleProjectDTO {

    /**
     * ID,sample_project表主键
     */
    private Long id;

    /**
     * 截止日期，格式为 "yyyy-MM-dd HH:mm:ss"。
     */
    private LocalDateTime deadline;

    /**
     * 样本名称。
     */
    private String name;

    /**
     * 样本 ID。主键
     */
    private Long sampleId;

    /**
     * 样本提交者id
     */
    private Long sampleSubmitterUserId;

    /**
     * 告警剩余小时数。
     */
    private String remainingHour;

    /**
     * 项目名称。
     */
    private String projectName;
}