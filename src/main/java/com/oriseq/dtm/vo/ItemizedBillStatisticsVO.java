package com.oriseq.dtm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/9 9:17
 */
@Data
public class ItemizedBillStatisticsVO {

    private List<Long> projectIds;

    /**
     * 送检单位
     */
    private Long useGroupId;

    /**
     * 创建实际的范围
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> creationTime;

}
