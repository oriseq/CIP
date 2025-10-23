package com.oriseq.controller.basis;

import com.oriseq.common.log.EnableLogging;
import com.oriseq.controller.utils.JwtUtils;
import com.oriseq.controller.utils.Result;
import com.oriseq.dtm.entity.SysConfig;
import com.oriseq.service.SysConfigService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Description:  系统配置表
 *
 * @author hacah
 * @version 1.0
 * @date 2025/9/19 10:21
 */
@RestController
@RequestMapping("sysConfig")
@EnableLogging
@Slf4j
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 查询所有系统配置
     *
     * @return 所有系统配置列表
     */
    @GetMapping("/list")
    public Result<List<SysConfig>> getAllConfigs(HttpServletRequest request) {
        // 校验token
        String token = request.getHeader("Authorization");
        Boolean result = JwtUtils.validateToken(token);
        List<SysConfig> list = sysConfigService.list();
        if (!result) {
            // 未登录的情况
            // 过滤出登录页的配置、主页配置返回，用于登录页渲染页面
            list = list.stream()
                    .filter(config -> config.getConfigKey().startsWith("login.") ||
                            config.getConfigKey().startsWith("site."))
                    .toList();
        }
        return Result.defaultSuccessByMessageAndData("查询成功", list);
    }


    /***
     * 根据名称获取系统配置。 暂不使用， 因为配置项比较少， 直接查询数据库即可。
     *
     * @param configName 系统配置名称
     * @return 系统配置对象
     */
    @GetMapping("/name")
    public Result<SysConfig> getConfigByName(@RequestParam("configName") String configName) {
        SysConfig config = sysConfigService.getByName(configName);
        return Result.defaultSuccessByMessageAndData("查询成功", config);
    }

    /**
     * 根据配置键批量修改配置值
     *
     * @param configMap 包含配置键值对的映射
     * @return 是否更新成功
     */
    @PutMapping("/batch")
    public Result<Object> updateBatchByConfigKey(@RequestBody Map<String, String> configMap) {
        sysConfigService.updateBatchByNameMap(configMap);

        // 批量更新
        return Result.defaultSuccessByMessage("更新成功");
    }
}