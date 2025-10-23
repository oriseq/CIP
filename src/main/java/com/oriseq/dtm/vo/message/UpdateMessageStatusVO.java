package com.oriseq.dtm.vo.message;

import lombok.Data;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/10 9:49
 */
@Data
public class UpdateMessageStatusVO {

    private List<Long> ids;

    /**
     * 是否查收全部
     */
    private Boolean checkAll;

}
