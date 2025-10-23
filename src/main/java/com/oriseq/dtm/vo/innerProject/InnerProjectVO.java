package com.oriseq.dtm.vo.innerProject;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huang
 * @date 2024/05/21
 */
@Data
public class InnerProjectVO {
    private Long id;
    private String projectName;
    private Integer isSingleItem;
    private String reportDate;
    private String detectMethod;
    private String consumableType;
    private Long projectCategoryId;
    /**
     * 价格
     */
    private BigDecimal price;
}