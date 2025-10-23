package com.oriseq.dtm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huang
 */
@Data
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String phoneNumber;
    private String mail;
    private String password;
    private Long userGroupId;
    private Integer isSuper;
    private Integer isGroupSuper;
    /**
     * 可用状态
     */
    private Boolean avail;
    /**
     * 邀请码
     */
    private String invitationCode;
    /**
     * 真实姓名
     */
    private String realName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime creationTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastLoginTime;
    private String userGroupName;
}
