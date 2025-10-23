package com.oriseq.dtm.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ProjectPriceDTO {

    /** 总价格 */
    private BigDecimal totalPrice;

    /** 折扣后总价 */
    private BigDecimal totalDiscountedPrice;


}