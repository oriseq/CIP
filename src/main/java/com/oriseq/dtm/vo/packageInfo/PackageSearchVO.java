package com.oriseq.dtm.vo.packageInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Description: 查询条件
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class PackageSearchVO {

    /**
     * 套餐名
     */
    @NotBlank
    private String packageName;

    /**
     * 项目ids
     */
    @NotEmpty
    private List<Long> projectIds;

    /**
     * 组id
     */
    private String userGroupId;

    private String remarks;


}
