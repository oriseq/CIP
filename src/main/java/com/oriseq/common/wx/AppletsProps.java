package com.oriseq.common.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/27 17:06
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class AppletsProps {

    private String appID;
    private String appSecret;
}
