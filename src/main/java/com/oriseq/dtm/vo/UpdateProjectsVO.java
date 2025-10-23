package com.oriseq.dtm.vo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/3 13:43
 */
@Data
public class UpdateProjectsVO {

    /**
     * 样本id
     */
    @NotNull
    private Long id;

    @NotEmpty
    private List<Project> projects;

    @Data
    public static class Project {
        private Long id;
        private Integer status;
    }

}
