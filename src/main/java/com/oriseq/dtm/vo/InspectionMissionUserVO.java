package com.oriseq.dtm.vo;

import lombok.Data;

import java.util.List;

/**
 * Description: 返回VO
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/19 10:18
 */
@Data
public class InspectionMissionUserVO {
    private Long id;
    private String name;
    private List<InspectionMissionUserVO> children;
}
