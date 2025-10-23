package com.oriseq.dtm.vo.billStatistics;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/9 10:33
 */
@Data
public class BillStatisticsRespVO {

    private String projectName;
    private String timeGroup;
    private Integer totalNum;
    /**
     * 标准收费
     */
    private BigDecimal price;
    private BigDecimal totalPrice;

    /**
     * 组划分的组数据
     * key；组id，value：组数据
     */
    private Map<Long, GroupData> groupDataMap = new HashMap<>();

    @Data
    public static class GroupData {
        /**
         * 送检量
         */
        private Integer num;
        /**
         * 总费用
         */
        private BigDecimal totalPrice;
        /**
         * 结算单价
         */
        private BigDecimal settlementPrice;
    }
}
