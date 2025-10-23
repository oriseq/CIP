package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huang
 */
@Data
@TableName("user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String phoneNumber;
    private String mail;
    private String password;
    private Long userGroupId;
    private Integer isSuper;
    private Integer isGroupSuper;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 可用状态
     */
    private Boolean avail;
    private LocalDateTime creationTime;
    private LocalDateTime lastLoginTime;
}
