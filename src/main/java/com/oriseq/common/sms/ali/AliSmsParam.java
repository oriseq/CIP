package com.oriseq.common.sms.ali;


import lombok.Data;

@Data
public class AliSmsParam {
    private String signName;
    private String templateCode;
    private String phoneNumbers;
    private String templateParam;

}