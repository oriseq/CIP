package com.oriseq.dtm.vo.sample;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SampleUpdateVO {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    /**
     * 1:男 2：女 3：未知
     */
    @NotNull
    @Positive
    private Integer sex;
    @NotNull
    @PositiveOrZero
    private Integer age;
    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate birthday;
    private List<Project> projects;
    /**
     * 物流单号
     */
    private String logisticsTrackingNumber;

    /**
     * 手机号后四位
     */
    private String phoneNumLastFour;

    /**
     * 样本编号
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

    /**
     * 备注
     */
    private String remarks;

    @Data
    public static class Project {
        private Long id;
        private String name;
        private Integer status;
        private String consumableType;

    }

}
