package com.oriseq.common.sms.ali;

import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Hacah
 * @className: SmsUtils
 * @description: 短信工具
 * @date 2024/4/29 16:38
 */
@Slf4j
@Component
public class SmsTool {

    @Autowired
    private AsyncClient asyncClient;


    public void sendSms(AliSmsParam aliSmsParam) throws ExecutionException, InterruptedException {
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(aliSmsParam.getSignName()) // SampleOS系统的短信服务签名（固定）
                .templateCode(aliSmsParam.getTemplateCode()) // SampleOS系统的登录验证码发送模板（固定）
                .phoneNumbers(aliSmsParam.getPhoneNumbers()) // 填写待发送的手机号
                .templateParam("{\"code\":\"" + aliSmsParam.getTemplateParam() + "\"}") // 填写验证码
                .templateParam("{\"code\":\"" + aliSmsParam.getTemplateParam() + "\"}") // 填写
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = asyncClient.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        SendSmsResponse resp = response.get();
        log.debug("发送短信后的返回值：{}", new Gson().toJson(resp.getBody()));
        // Asynchronous processing of return values
        /*response.thenAccept(resp -> {
            System.out.println(new Gson().toJson(resp));
        }).exceptionally(throwable -> { // Handling exceptions
            System.out.println(throwable.getMessage());
            return null;
        });*/

        // Finally, close the client
//        client.close();

    }

    @PreDestroy
    public void closeAsyncClient() {
        // 在 bean 销毁前调用 asyncClient.close() 方法
        asyncClient.close();
    }


}
