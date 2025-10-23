package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Description: 消息提醒
 *
 * @author hacah
 * @version 1.0
 */
@ToString
@Data
public class MessageNotification {
    @TableId(value = "id", type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    private Long id;
    private String avatar;
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime datetime;
    /**
     * 消息类型 通知：1、消息：2、待办：3
     */
    private Integer type;
    /**
     * 状态 0：未读 1：已读 2:删除
     */
    private Integer status;
    private String extra;
    private String color;
    private Long userId;
    private String secondaryType;

}
