package com.oriseq.dtm.dto;

import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * 用户信息返回VO
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/14 11:48
 */
@Data
public class LoginUser {
    private List<Role> roles;
    private String userId;
    private String username;
    private String phone;
    private String homePath;
    private String token;
    private String desc;
    private String avatar;
    private String userGroupName;
    private Long userGroupId;
    private Integer isSuper;
    private Integer isGroupSuper;
    /**
     * 用户组可用状态， true可用、false禁止
     */
    private Boolean availStatusGroup;

    /**
     * 是否内部组
     */
    private Boolean isInternalGroup;

    /**
     * 真实姓名
     */
    private String realName;

    @Override
    public String toString() {
        return "UserVO{" +
                "roles=" + roles +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    // Optional: Override toString() for UserVO class if needed

    public boolean isSuper() {
        if (Optional.ofNullable(this.getIsSuper()).orElse(0) == 1) {
            return true;
        }
        return false;
    }

    public boolean isInternalGroup() {
        return Optional.ofNullable(this.getIsInternalGroup()).orElse(false);
    }

    public boolean isGroupSuper() {
        if (Optional.ofNullable(this.getIsGroupSuper()).orElse(0) == 1) {
            return true;
        }
        return false;
    }

    @Data
    public static class Role {
        private String roleName;
        private String value;

        // Getters and setters

        // Optional: Override toString() for Role class if needed
        @Override
        public String toString() {
            return "Role{" +
                    "roleName='" + roleName + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
