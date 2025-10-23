package com.oriseq.dtm.vo.personalSettings;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Description: 请求对象，用于修改密码
 *
 * @author hacah
 * @version 1.0
 * @date 2025/2/27 16:07
 */
@Data
public class ChangePasswordVO {


    /**
     * 旧密码（必填）
     */
    @NotBlank
    private String oldPassword;

    /**
     * 新密码（必填）
     */
    @NotBlank
    private String newPassword;

    /**
     * 确认密码（必填）
     */
    @NotBlank
    private String confirmPassword;
}
