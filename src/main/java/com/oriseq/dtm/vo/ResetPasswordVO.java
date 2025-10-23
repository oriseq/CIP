package com.oriseq.dtm.vo;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author huang
 */
@Data
public class ResetPasswordVO {
    @NotBlank
    private String phoneOrMail;
    @NotBlank
    private String verificationCode;
    @NotBlank
    private String newPassword;

}