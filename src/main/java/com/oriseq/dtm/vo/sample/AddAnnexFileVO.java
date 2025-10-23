package com.oriseq.dtm.vo.sample;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增附件参数
 */
@Data
public class AddAnnexFileVO {
    @NotNull
    private Long sampleId;
    @NotBlank
    private String fileId;

}
