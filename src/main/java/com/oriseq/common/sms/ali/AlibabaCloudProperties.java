package com.oriseq.common.sms.ali;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alibaba-cloud-sms")
@Data
public class AlibabaCloudProperties {
    private String accessKeyId;
    private String accessKeySecret;
}
