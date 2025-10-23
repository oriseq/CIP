package com.oriseq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.entity.SysConfig;

import java.util.Map;

/**
 * @author huang
 * @description 针对表【sys_config(系统配置表)】的数据库操作Service
 * @createDate 2025-09-19 10:08:37
 */
public interface SysConfigService extends IService<SysConfig> {
    /**
     * 根据名称获取系统配置
     *
     * @param configName
     * @return
     */
    SysConfig getByName(String configName);

    /**
     * 根据名称批量更新系统配置
     *
     * @param configMap
     */
    void updateBatchByNameMap(Map<String, String> configMap);
}
