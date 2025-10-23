package com.oriseq.dtm.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: VO 得到all消息
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/9 13:17
 */
@ToString
@Data
public class MessageAllVO {

//    @NotNull
//    private Long userId;
    /**
     * 消息类型 通知：1，消息：2，待办：3
     */
//    @NotNull
    private Integer type;

    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private List<LocalDateTime> datetime;
    private String secondaryType;
    /**
     * 查收类型
     * 未查收:0
     * 对应status等于0或1
     * 已查收:1
     * 对应status等于2
     */
    private Integer externalState;
//    @NotBlank


}
