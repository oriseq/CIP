package com.oriseq.dtm.vo.packageInfo;

import jakarta.validation.constraints.NotEmpty;
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
public class PackageDeleteVO {

    /**
     * 套餐ids
     */
    @NotEmpty
    private List<Long> packageIds;


}
