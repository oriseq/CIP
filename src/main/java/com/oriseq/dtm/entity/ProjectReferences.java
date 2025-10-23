package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目引用表
 *
 * @TableName project_references
 */
@TableName("project_references")
@Data
@ToString
public class ProjectReferences implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 引用索引ID（key）
     */
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 用户组ID
     */
    private Long userGroupId;
    /**
     * 折扣系数
     */
    private BigDecimal priceCoefficient;
    /**
     * 默认外送单位id
     */
    @TableField("default_delivery_unit_id")
    private Long defaultDeliveryUnitId;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime creationTime;
    /**
     *
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}