package com.oriseq.dtm.vo;

import lombok.Data;

import java.util.List;

/**
 * 登录和获取用户信息返回VO
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/14 11:48
 */
@Data
public class LoginUserVO {
    private List<Role> roles;
    private String userId;
    private String username;
    private String phone;
    private String homePath;
    private String token;
    private String desc;
    private String avatar;
    private String userGroupName;
    private Boolean isSuper;
    private Boolean isGroupSuper;
    /**
     * 是否内部组
     */
    private Boolean isInternalGroup;
    private String realName;
    private String email;


    @Data
    public static class Role {
        private String roleName;
        private String value;
    }

}
