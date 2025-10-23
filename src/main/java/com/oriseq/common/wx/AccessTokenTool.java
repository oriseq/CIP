package com.oriseq.common.wx;

import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description: 维护accessToken
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/28 13:34
 */
@Component
public class AccessTokenTool {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AppletsProps appletsProps;

    /**
     * 获取accessToken
     *
     * @return
     */
    public String getAccessToken() {
        String s = stringRedisTemplate.opsForValue().get("wx-accessToken");
        if (s == null || "".equals(s)) {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("appid", appletsProps.getAppID());
            paramMap.put("secret", appletsProps.getAppSecret());
            paramMap.put("grant_type", "client_credential");
            String result = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", paramMap);

            Gson gson = new Gson();
            HashMap hashMap = gson.fromJson(result, HashMap.class);
            String accessToken = (String) hashMap.get("access_token");
            Double expires = (Double) hashMap.get("expires_in");
            // token有效期为7200s
            stringRedisTemplate.opsForValue().set("wx-accessToken", accessToken, expires.longValue(), TimeUnit.SECONDS);
            return accessToken;
        }
        return s;
    }

}
