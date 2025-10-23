package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author huang
 */
@Data
@ToString
@TableName("user_group")
public class UserGroup {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String groupName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime creationTime;
    private Long amountOfUsers;
    /**
     * 可用状态
     */
    private Boolean availStatus;

    /**
     * 是否内部组
     */
    @TableField("is_internal_group")
    private Boolean isInternalGroup;

}