package com.oriseq.dtm.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GroupUsersCascaderDTO {
    private String groupName;
    private Long userGroupId;
    private String username;
    private String realName;
    private Long userId;

    // Getters, setters, and constructors
}