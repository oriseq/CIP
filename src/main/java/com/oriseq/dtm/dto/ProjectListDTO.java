package com.oriseq.dtm.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;


/**
 * 用于项目管理，查询项目list 返回
 */
@Data
@ToString
public class ProjectListDTO {

    private Long id;

    /**
     * 项目id
     */
    private Long originProjectId;

    /** 项目名称 */
    private String projectName;

    /** 检测方法 */
    private String detectMethod;

    /** 采样耗材类型 */
    private String consumableType;

    /** 所属用户组 */
    private String userGroupName;

    /** 价格 */
    private BigDecimal price;

    /** 折扣系数 */
    private BigDecimal priceCoefficient;

    /** 折后价格 */
    private BigDecimal discountedPrice;

    /**
     * 默认外送单位id
     */
    private Long defaultDeliveryUnitId;
    /**
     * 默认外送单位名称
     */
    private String defaultDeliveryUnitName;


}