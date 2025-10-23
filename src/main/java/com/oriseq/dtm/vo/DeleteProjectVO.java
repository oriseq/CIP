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
 * @date 2024/6/19 15:36
 */
@Data
public class DeleteProjectVO {

    @NotNull
    private Long sampleId;

    @NotEmpty
    private List<String> ids;
}
