package com.oriseq.dtm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * 样本基础信息
 */
@Data
@ToString
public class SampleBasicInfo {
    private Long id;
    private String name;
    private Integer sex;
    private Integer age;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;
    /**
     * 物流单号
     */
    private String logisticsTrackingNumber;

    /**
     * 手机号后四位
     */
    private String phoneNumLastFour;
    private List<ProjectStatusStatistics> projectStatusStatistics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectStatusStatistics {

        /**
         * 状态
         */
        private Integer status;
        /**
         * 次数
         */
        private Integer count;

    }

}