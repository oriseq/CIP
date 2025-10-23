package com.oriseq.dtm.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProjectsRequestVO {


    /**
     * sampleid
     */
    @NotBlank
    private String id;
    private List<Long> projects;
    private Integer status;


    /**
     * 排序列
     */
    private String field;

    /**
     * 排序方式
     * ascend
     * descend
     */
    private String order;

    /**
     * 备注
     */
    private String remarks;

}