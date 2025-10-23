package com.oriseq.dtm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProjectItemizedSpendDTO {
    private String sampleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime creationTime;
    private String name;
    private String projectName;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private String sampleRemarks;
    private String projectRemarks;

}
