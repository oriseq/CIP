package com.oriseq.dtm.vo;

import lombok.Data;

import java.util.List;

/**
 * Description: 套餐vo
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 14:06
 */
@Data
public class PackageVO {

    private Long id;
    private String packageName;
    private List<Projects> projects;

    @Data
    public static class Projects {
        private Long id;
        private String projectName;
        /**
         * 状态
         * - 取消:0
         * - 待定:1
         * - 确认检测:2
         * - 正在检测:3
         * - 报告路径为空
         * - 已完成:4
         * - 报告路径非空
         */
        private Integer status;
        /**
         * 耗材
         */
        private String consumableType;
    }

}
