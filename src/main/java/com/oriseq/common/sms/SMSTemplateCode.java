package com.oriseq.common.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信配置
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/11 15:39
 */
@Component
@ConfigurationProperties(prefix = "sms")
@Data
public class SMSTemplateCode {

    private Map<String, String> templateCode;
}
