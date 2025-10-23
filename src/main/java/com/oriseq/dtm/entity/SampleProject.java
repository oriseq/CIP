package com.oriseq.dtm.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sample_project")
public class SampleProject {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sampleId;
    private Long projectId;
    private Integer projectStatus;
    private String fileId;
    /**
     * 阴阳性：0：阴，1阳
     */
    private String polarity;
    /**
     * 复核结果：正常，异常
     */
    private String reviewResults;
    /**
     * 外送单位
     */
    private String deliveryUnit;
    /**
     * 外送人
     */
    private String deliveryPerson;
    /**
     * 结果图
     */
    private String resultImg;
    /**
     * 备注
     */
    private String remarks;
    private LocalDateTime deadline;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime creationTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    @TableField("is_alarm_sent")
    private Boolean isAlarmSent;
    @TableLogic
    @TableField("is_del")
    private Integer isDel;
}