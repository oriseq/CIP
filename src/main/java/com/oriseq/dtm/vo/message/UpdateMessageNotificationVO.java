package com.oriseq.dtm.vo.message;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

/**
 * Description: 更新消息
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/9 13:17
 */
@ToString
@Data
public class UpdateMessageNotificationVO {

    @NotNull
    private Long id;
    private String avatar;
    private String title;
    private String description;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime datetime;
    @NotNull
    private Integer type;
    private String extra;
    private String color;
//    @NotNull
//    private Integer userId;

}
