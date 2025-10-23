package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/18 16:49
 */
@Data
public class ProjectReviewVO {

    @NotNull
    private Long sampleId;
    /**
     * 项目ids
     */
    @NotEmpty
    private List<Long> projectIds;
    /**
     * 复核结果（正常，异常）
     */
    @NotBlank
    private String reviewResults;

}
