package com.oriseq.dtm.vo.sample;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SampleReportIdUpdateVO {

    @NotNull
    private Long sampleId;

    @NotEmpty
    private List<String> reportIds;

    @NotEmpty
    private List<Long> sampleProjectIds;


}
