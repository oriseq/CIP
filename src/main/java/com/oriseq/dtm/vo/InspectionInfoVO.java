package com.oriseq.dtm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oriseq.dtm.entity.FileInfo;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


/**
 * 送检信息
 *
 * @author huang
 * @date 2024/05/21
 */
@Data
public class InspectionInfoVO {
    @NotBlank
    private String name;
    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDate birthday;
    @NotNull
    @PositiveOrZero
    private Integer age;
    /**
     * isBirthInput是否使用出生日期生成  1:是 0:否
     */
    @NotNull
    private Integer isBirthInput;
    /**
     * 1:男 2：女 3：未知
     */
    @NotNull
    @Positive
    private Integer sex;
    private List<Projects> selectedProjects;

    /**
     * 文件路径
     */
    private List<FileInfo> fieldsc;
    /**
     * 物流单号
     */
    private String logisticsTrackingNumber;

    /**
     * 手机号后四位
     */
    private String phoneNumLastFour;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 样本编号（业务）
     */
    private String sampleId;

    /**
     * 样本归属用户组ID
     */
    private Long sampleUserGroupId;
    /**
     * 当天提交编号
     */
    private String submitNumberToday;

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
         * 项目默认的外送单位
         */
        private String deliveryUnit;
    }

}