package com.oriseq.dtm.vo;

import jakarta.validation.constraints.NotEmpty;
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
public class NoHostingVO {
    @NotEmpty
    private List<String> ids;
}
