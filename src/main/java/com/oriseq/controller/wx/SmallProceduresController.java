package com.oriseq.controller.wx;

import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.oriseq.common.wx.AccessTokenTool;
import com.oriseq.common.wx.AppletsProps;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.vo.LoginUserVO;
import com.oriseq.dtm.vo.WxLoginVO;
import com.oriseq.service.ISmallProceduresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 小程序接口
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/27 16:55
 */
@RestController
@RequestMapping("/wx")
@Slf4j
public class SmallProceduresController {

    @Autowired
    private AppletsProps appletsProps;

    @Autowired
    private AccessTokenTool accessTokenTool;

    @Autowired
    private ISmallProceduresService smallProceduresService;

    @PostMapping("/login")
    public String login(@RequestBody WxLoginVO wxLoginVO) {
        log.info("wxLoginVO:{}", wxLoginVO);
//        Object code = map.get("code");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("js_code", wxLoginVO.getCode());
        paramMap.put("appid", appletsProps.getAppID());
        paramMap.put("secret", appletsProps.getAppSecret());
        paramMap.put("grant_type", "authorization_code");

        String result = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session", paramMap);

        log.info("请求结果：{}", result);
        return result;
    }

    @PostMapping("/loginReal")
    public Result<Object> loginReal(@RequestBody HashMap map) {
        log.info("map:{}", map);
        Object code = map.get("code");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", code);
        paramMap.put("access_token", accessTokenTool.getAccessToken());
        String result = HttpUtil.post("https://api.weixin.qq.com/wxa/business/getuserphonenumber", paramMap);

        // 处理结果
        Map rsp = new Gson().fromJson(result, Map.class);
        LoginUserVO loginUserVO = smallProceduresService.setUserInfo(rsp);
        if (loginUserVO != null) {
            log.info("请求结果：{}", result);
            return Result.defaultSuccessByMessageAndData("登录成功", loginUserVO);
        }

        return Result.defaultErrorByMessageAndData("请求小程序接口失败", result);
    }

}
