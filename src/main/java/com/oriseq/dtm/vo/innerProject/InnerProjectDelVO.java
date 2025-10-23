package com.oriseq.dtm.vo.innerProject;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * Description: 删除
 *
 * @author hacah
 * @version 1.0
 */
@Data
public class InnerProjectDelVO {

    /**
     * 项目ids
     */
    @NotEmpty
    private List<Long> projectIds;


}
