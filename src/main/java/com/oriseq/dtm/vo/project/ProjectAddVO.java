package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 删除
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class ProjectAddVO {

    /**
     * 项目引用ids
     */
    @NotEmpty
    private List<Long> projectIds;

    private Long userGroupId;

    @Digits(integer = 8, fraction = 2, message = "价格系数必须为最多 10 位数字，其中小数部分最多 2 位")
    private BigDecimal priceCoefficient;

    /**
     * 默认外送单位id
     */
    private Long defaultDeliveryUnitId;


}
