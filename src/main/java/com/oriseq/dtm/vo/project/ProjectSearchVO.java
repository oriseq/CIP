package com.oriseq.dtm.vo.project;

import lombok.Data;

import java.util.List;

/**
 * Description: 项目管理list，查询条件
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class ProjectSearchVO {

    /**
     * 项目ids
     */
    private List<Long> projectIds;

    /**
     * 所属套餐
     */
    private Long belongingPackage;

    /**
     * 组id
     */
    private String userGroupId;


}
