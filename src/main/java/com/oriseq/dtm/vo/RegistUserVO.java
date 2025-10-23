package com.oriseq.dtm.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

/**
 * @author Hacah
 * @className: RegistUserVO
 * @description: 注册用户VO
 * @date 2024/4/30 13:54
 */
@Data
@ToString
public class RegistUserVO {

    /**
     * 手机号（必填）
     */
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "手机号格式错误")
    private String phone;
    @Email
    private String email;    // 邮箱（选填）
    /**
     * 可以包含字母、数字、下划线和中文字符，但不含@且不是纯数字
     */
    @Pattern(regexp = "^(?!.*@)(?!\\d+$)[\\w\\u4e00-\\u9fa5]+$", message = "用户名格式错误，用户名不含@且不是纯数字")
    private String username;

    private String realName;
    /**
     * 单位邀请码
     */
    @NotBlank
    private String inviteCode;
    /**
     * 密码
     */
    @NotBlank
    private String password;

    public RegistUserVO() {
        // 默认构造函数逻辑
    }
}
