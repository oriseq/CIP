package com.oriseq.dtm.vo;

import lombok.Data;
import lombok.ToString;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/6/11 14:52
 */
@Data
@ToString
public class AddUserGroupVO {

    private Long id;

    private String groupName;

    /**
     * 可用状态
     */
    private Boolean availStatus;

    /**
     * 是否内部组
     */
    private Boolean isInternalGroup;
}
