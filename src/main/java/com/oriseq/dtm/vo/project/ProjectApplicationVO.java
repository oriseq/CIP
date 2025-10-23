package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Description: 项目申请
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class ProjectApplicationVO {

    /**
     * 项目ids
     */
    @NotEmpty
    private List<Long> projectIds;


}
