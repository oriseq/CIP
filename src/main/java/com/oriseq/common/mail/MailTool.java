package com.oriseq.common.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hacah
 * @className: MailTool
 * @description: SampleOS的邮箱发送工具
 * @date 2024/5/7 14:24
 */
@Component
public class MailTool {

    @Autowired
    private MailService mailService;

    public void sendMail(String to, String subject, String content) {
        mailService.sendSimpleMail(to, subject, content);
    }


    public void sendReSetPasswordMail(String to, String verificationCode) {
        String subject = "CIP-密码重置";
        String content = "您的验证码为：" + verificationCode + "，请在5分钟内完成验证。您正在进行CIP的重置密码操作，请确保本人操作！！";
        mailService.sendSimpleMail(to, subject, content);
    }
}
