package com.oriseq.dtm.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 样本信息导入excel类
 */
@Data
public class SampleInfoSheetData {
    private String title;
    @NotNull
    @NotEmpty
    @Valid
    private List<SampleInfo> dataSource;
    private List<Column> columns;

    // Getters, setters, and constructors

    @Data
    public static class SampleInfo {
        //        @NotBlank
        private String name;
        /**
         * 1:男 2：女 3：未知
         */
//        @NotNull
//        @Positive
        private Integer sex;
        //        @NotNull
//        @PositiveOrZero
        private Integer age;
        //        @NotNull
//        @Past
//        @JsonFormat(pattern = "yyyy-MM-dd")
        private String birthday;
        //        private String projects;
        private String logisticsInformation;

        // Getters, setters, and constructors
    }

    @Data
    public static class Column {
        private String title;
        private String dataIndex;

        // Getters, setters, and constructors
    }
}



