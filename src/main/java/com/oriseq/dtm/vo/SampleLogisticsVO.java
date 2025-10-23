package com.oriseq.dtm.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class SampleLogisticsVO {
    @NotEmpty
    private List<String> ids;
    @NotBlank
    private String logisticsInfo;

}
