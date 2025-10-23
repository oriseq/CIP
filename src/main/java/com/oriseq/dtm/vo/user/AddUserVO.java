package com.oriseq.dtm.vo.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author Hacah
 * @className: AddUserVO
 * @description: 用户管理-新增用户VO
 */
@Data
@ToString
@NoArgsConstructor
public class AddUserVO {

    private Long id;

    /**
     * 手机号（必填）
     */
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "手机号格式错误")
    private String phoneNumber;

    @Email
    private String mail;    // 邮箱（选填）
    /**
     * 可以包含字母、数字、下划线和中文字符，但不含@且不是纯数字
     */
    @Pattern(regexp = "^(?!.*@)(?!\\d+$)[\\w\\u4e00-\\u9fa5]+$", message = "用户名格式错误，用户名不含@且不是纯数字")
    private String username;
    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 是否禁用
     */
    private Boolean avail;

    /**
     * 用户组id
     */
    private Long userGroupId;

    private Integer isSuper;

    private Integer isGroupSuper;

    private List<Long> permissionIds;

    /**
     * 真实姓名
     */
    private String realName;

}
