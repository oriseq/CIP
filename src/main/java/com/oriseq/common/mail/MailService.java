package com.oriseq.common.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author hacah
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 邮件发送人
     */
    @Value("${spring.mail.username}")
    private String from;

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        // 邮件主题
        message.setSubject(subject);
        // 邮件内容
        message.setText(content);
        mailSender.send(message);
    }
}