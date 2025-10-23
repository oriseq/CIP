package com.oriseq.dtm.vo.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description: 项目
 *
 * @author hacah
 * @version 1.0
 */
@Data
public class ProjectIdsByGroupVO {

    /**
     * 用户组id
     */
    @NotNull
    private Long userGroupId;


}
