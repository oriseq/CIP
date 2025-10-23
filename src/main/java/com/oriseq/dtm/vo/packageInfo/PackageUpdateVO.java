package com.oriseq.dtm.vo.packageInfo;

import lombok.Data;

import java.util.List;

/**
 * Description: 参数
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class PackageUpdateVO {

    private Long id;

    /**
     * 套餐名
     */
    private String packageName;

    /**
     * 项目ids
     */
    private List<Long> projectIds;

    /**
     * 用户组id
     */
    private Long userGroupId;

    private String remarks;


}
