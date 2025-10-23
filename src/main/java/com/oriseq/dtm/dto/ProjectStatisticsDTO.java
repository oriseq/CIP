package com.oriseq.dtm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProjectStatisticsDTO {

    private Long projectId;

    private String projectName;

    private Long sampleUserGroupId;

    private String groupName;

    private BigDecimal price;

    /**
     * 价格（折后）
     */
    private BigDecimal discountedPrice;

    private LocalDateTime creationTime;

    // Getters and Setters
}