package com.oriseq.dtm.vo;

import lombok.Data;

/**
 * Description: 项目信息 小程序
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/22 16:34
 */
@Data
public class ProjectVOForWX {
    private Long id;
    private String name;
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
    private String status;
    private Boolean iconValue;
    /**
     * 耗材
     */
    private String consumableType;

}
