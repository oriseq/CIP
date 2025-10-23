package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/19 15:36
 */
@Data
public class UpdateProjectStatusVO {

    @NotNull
    private Long sampleId;
    @NotNull
    private Long projectId;
    @NotNull
    private Integer projectStatus;

    /**
     * 阴阳性
     */
    private String polarity;

    /**
     * 外送单位
     */
    private String deliveryUnit;
    /**
     * 外送人
     */
    private String deliveryPerson;
    /**
     * 审核图
     */
    private List<String> resultImgList;
    /**
     * 备注
     */
    private String remarks;

}
