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
 * @date 2024/6/18 16:49
 */
@Data
public class SampleTransferVO {
    /**
     * 样本id集合
     */
    @NotEmpty
    private List<String> ids;
    /**
     * 过户id，用户id
     */
    @NotNull
    private Long sampleTransferId;

}
