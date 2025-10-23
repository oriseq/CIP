package com.oriseq.dtm.vo.sample;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ChangeSampleProjectsVO {

    @NotEmpty
    private List<Long> sampleProjectIds;
    @NotNull
    private Integer status;


}
