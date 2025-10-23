package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description: 更新
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class ProjectUpdateVO {

    /**
     * 项目引用id
     */
    @NotNull
    private Long projectId;

    /**
     * 价格
     */
//    @NotNull
//    private BigDecimal price;

    /**
     * 价格系数
     */
    private BigDecimal priceCoefficient;

    /**
     * 默认外送单位
     */
    private Long defaultDeliveryUnitId;


}
