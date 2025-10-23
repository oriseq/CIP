package com.oriseq.dtm.vo;

import com.oriseq.dtm.dto.ProjectDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Description: 项目信息
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/22 16:34
 */
@Data
public class ProjectVO {

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
    private Integer status;
    /**
     * 耗材
     */
    private String consumableType;

    /**
     * 提交次数
     */
    private Long submissionTimes;

    /**
     * 用来记录用户选中的，用来提交表单
     */
    private String deliveryUnit;

    /**
     * 用于点击切换和默认选择
     */
    private ProjectDTO.DeliveryUnitSetting deliveryUnitSetting;


    @Data
    public static class DeliveryUnitSetting {
        /**
         * 用于点击切换
         */
        private List<String> options;

        /**
         * 用于默认选择
         */
        private Map<String, String> defaultOption;
    }
}
