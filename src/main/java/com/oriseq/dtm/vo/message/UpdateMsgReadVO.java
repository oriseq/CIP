package com.oriseq.dtm.vo.message;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/10 9:49
 */
@Data
public class UpdateMsgReadVO {

    @NotNull
    private Long userId;

    /**
     * 消息类型 通知：1、消息：2、待办：3
     */
    @NotNull
    private Integer type;

}
