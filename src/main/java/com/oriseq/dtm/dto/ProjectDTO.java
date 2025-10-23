package com.oriseq.dtm.dto;

import cn.hutool.json.JSONArray;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Description: 项目，包含分类信息
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class ProjectDTO {

    /**
     * 项目分类id
     */
    private Long pcId;
    private String name;
    private Long parentId;
    private Long id;
    private String projectName;
    private Integer isSingleItem;
    private String reportDate;
    private String detectMethod;
    private String consumableType;
    private Long projectCategoryId;
    /**
     * 外送单位
     */
    private JSONArray deliveryUnitIds;
    /**
     * 用来记录用户选中的，用来提交表单
     */
    private String deliveryUnit;

    /**
     * 用于点击切换和默认选择
     */
    private DeliveryUnitSetting deliveryUnitSetting;


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




