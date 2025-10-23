package com.oriseq.dtm.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 上传报告
 */
@Data
public class UploadReportVO {
    @NotNull
    private Long sampleId;
    @NotNull
    private Long projectId;
    @NotNull
    private String fileId;
    @NotNull
    private String polarity;

}