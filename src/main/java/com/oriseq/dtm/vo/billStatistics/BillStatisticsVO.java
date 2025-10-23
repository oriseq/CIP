package com.oriseq.dtm.vo.billStatistics;

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
public class BillStatisticsVO {

    private List<Long> projectIds;

    /**
     * 时间分类
     * 不填就所有
     * 填 y：年；m：月；d：日
     */
    private String timeGroup;

    /**
     * 创建实际的范围
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private List<LocalDateTime> creationTime;

}
