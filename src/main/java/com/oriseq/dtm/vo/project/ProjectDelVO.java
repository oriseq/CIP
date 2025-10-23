package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Description: 删除
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:03
 */
@Data
public class ProjectDelVO {

    /**
     * 项目引用ids
     */
    @NotEmpty
    private List<Long> projectIds;


}
