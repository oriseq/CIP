package com.oriseq.dtm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/2 14:18
 */
@Data
public class ProjectManageDTO {
    private Long sampleProjectId;
    private Long projectId;
    private Integer projectStatus;
    private String projectName;
    /**
     * 文件id
     */
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

    /**
     * 截至时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deadline;

    /**
     * 仪器id
     */
    private Long instrumentId;

}
