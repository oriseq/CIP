package com.oriseq.dtm.vo.sample;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2025/3/26 11:28
 */
@Data
public class DelFileVO {

    @NotBlank
    private String fileId;


}
