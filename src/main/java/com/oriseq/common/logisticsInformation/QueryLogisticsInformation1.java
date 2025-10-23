package com.oriseq.common.logisticsInformation;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Description: 查询快递类
 *
 * @author hacah
 * @version 1.0
 * @date 2024/11/28 11:43
 */
@Slf4j
public class QueryLogisticsInformation1 {

    public JSONObject queryLogisticsInformation(String logisticsTrackingNumber, String phoneNumLastFour) {
        // 先{快递单号}查，不行就{快递单号:手机尾号}
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("captcha-token", "717eae");
        String mailNo = logisticsTrackingNumber;
        HttpResponse httpResponse = this.doHttp(paramMap, mailNo);
        String body = httpResponse.body();
        JSONObject entries = JSONUtil.parseObj(body);
        String errCode = entries.get("errCode").toString();
        /*
            1: message:查询不到信息，请核对单号和快递公司是否正确
            2: message:请输入【收件人】或【寄件人】\n电话号码后4位
        */
        if ("0".equals(errCode)) {
            // 快递单号查询成功
            return entries;
        } else {
            log.debug("1-body:{}", body);
            // 快递单号查询失败，再查{快递单号:手机尾号}
            mailNo = logisticsTrackingNumber + ":" + phoneNumLastFour;
            httpResponse = this.doHttp(paramMap, mailNo);
            body = httpResponse.body();
            entries = JSONUtil.parseObj(body);
            String errCode2 = entries.get("errCode").toString();
            if ("0".equals(errCode2)) {
                // 快递单号:手机尾号查询成功
                log.debug("快递单号:手机尾号查询成功");
                return entries;
            } else {
                log.debug("2-body:{}", body);
                // 快递单号:手机尾号查询失败
                log.warn("快递单号:手机尾号查询失败");
                return null;
            }
        }

    }

    /**
     * 发送请求
     *
     * @param paramMap 请求参数
     * @param mailNo   url上的参数
     * @return
     */
    private HttpResponse doHttp(HashMap<String, String> paramMap, String mailNo) {
        String url = "https://trace.fkdex.com/auto/" + mailNo + "/";
        HttpRequest xmlHttpRequest = HttpRequest.get(url)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                .header("X-Requested-With", "XMLHttpRequest")
                .formStr(paramMap);
        HttpResponse httpResponse = xmlHttpRequest.execute();
//        System.out.println("xmlHttpRequest:"+xmlHttpRequest);
//        log.debug("httpResponse:"+httpResponse);
        return httpResponse;
    }

}
