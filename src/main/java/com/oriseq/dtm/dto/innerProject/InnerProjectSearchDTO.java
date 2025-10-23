package com.oriseq.dtm.dto.innerProject;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.oriseq.dtm.entity.DeliveryUnit;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huang
 */
@Data
public class InnerProjectSearchDTO {
    private Long id;
    private String projectName;
    //    private Integer isSingleItem;
    private String detectMethod;
    private String consumableType;
    private Long projectCategoryId;
    private BigDecimal price;
    private String projectCategoryName;
    /**
     * 样本要求
     */
    private String sampleRequirements;
    /**
     * 时分刻度是否置末(1:是，0:否)
     */
    private Boolean isReportHourMinutesEnd;

    /**
     * 报告周期（文字记录）
     */
    private String reportDate;
    /**
     * 报告天数
     */
    private Integer reportDay;
    /**
     * 报告小时
     */
    private Integer reportHour;
    /**
     * 排除星期：[1,2,3,4,5,6,7]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray disableWeek;
    /**
     * 警报剩余时间（小时）
     */
    private String remainingHour;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 包含的外送单位ids
     */
    @TableField(value = "delivery_unit_ids", typeHandler = JacksonTypeHandler.class)
    private JSONArray deliveryUnitIds;

    /**
     * 外送单位数据
     */
    private List<DeliveryUnit> deliveryUnits;

    /**
     * 仪器id
     */
    private Long instrumentId;

    /**
     * 仪器id对应的名称
     */
    private String instrumentName;

}