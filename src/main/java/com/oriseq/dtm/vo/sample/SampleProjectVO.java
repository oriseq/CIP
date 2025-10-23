package com.oriseq.dtm.vo.sample;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SampleProjectVO {
    /**
     * 样本下的项目唯一编号
     */
    private Long sampleProjectId;
    private Long projectId;
    private Integer projectStatus;
    private String projectName;
    /**
     * 是否外送
     */
    private Boolean isDeliveryOutside;

    private LocalDateTime creationTime;

    /**
     * 外送单位
     */
    private String deliveryUnit;

    /**
     * 仪器id
     */
    private Long instrumentId;

}