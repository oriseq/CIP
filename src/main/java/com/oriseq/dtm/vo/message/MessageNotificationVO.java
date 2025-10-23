package com.oriseq.dtm.vo.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Description: 消息提醒
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/9 13:17
 */
@ToString
@Data
public class MessageNotificationVO {

    private String avatar;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime datetime;
    @NotNull
    private Integer type;
    private String extra;
    private String color;
    private String secondaryType;
    @NotEmpty
    private List<Long> userIds;

}
