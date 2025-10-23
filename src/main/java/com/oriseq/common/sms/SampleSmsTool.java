package com.oriseq.common.sms;

import com.oriseq.common.sms.ali.AliSmsParam;
import com.oriseq.common.sms.ali.SmsTool;
import com.oriseq.common.utils.RegexUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * @author Hacah
 * @className: SampleSmsTool
 * @description: 用于系统的实际发送短信工具
 * @date 2024/4/30 9:01
 */
@Component
public class SampleSmsTool {

    @Autowired
    private SmsTool smsTool;


    /**
     * 发送短信
     *
     * @param templateParam
     * @param phoneNumber
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void sendSmsForLogin(String templateParam, String phoneNumber, String templateCode) throws ExecutionException, InterruptedException {
        if (StringUtils.isBlank(templateParam) || StringUtils.isBlank(phoneNumber)) {
            throw new RuntimeException("参数不能空");
        }
        if (!RegexUtils.isPhoneNumber(phoneNumber)) {
            throw new RuntimeException("手机号码格式不对");
        }
        AliSmsParam aliSmsParam = new AliSmsParam();
        aliSmsParam.setTemplateParam(templateParam);
        aliSmsParam.setSignName("CIP");
        aliSmsParam.setTemplateCode(templateCode);
        aliSmsParam.setPhoneNumbers(phoneNumber);
        smsTool.sendSms(aliSmsParam);
    }

}
