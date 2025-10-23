package com.oriseq.dtm.vo;

import lombok.Data;
import lombok.ToString;


/**
 * 短信登录
 *
 * @author huang
 */
@Data
@ToString
public class SmsLoginVO {
    private String phone;
    private String verificationCode;
}
