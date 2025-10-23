package com.oriseq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.entity.SysConfig;
import com.oriseq.mapper.SysConfigMapper;
import com.oriseq.service.IFileInfoService;
import com.oriseq.service.SysConfigService;
import com.oriseq.service.strategy.FaviconFileHandlingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author huang
 * @description 针对表【sys_config(系统配置表)】的数据库操作Service实现
 * @createDate 2025-09-19 10:08:37
 */
@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
        implements SysConfigService {

    @Autowired
    private IFileInfoService fileInfoService;

    @Autowired
    private FaviconFileHandlingStrategy faviconFileHandlingStrategy;

    @Override
    public SysConfig getByName(String configName) {
        SysConfig config = baseMapper.selectOne(
                new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, configName)
        );
        return config;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBatchByNameMap(Map<String, String> configMap) {
        // 查询所有配置
        List<SysConfig> configs = baseMapper.selectList(null);
        // 更新匹配的配置项
        for (SysConfig config : configs) {
            String configKey = config.getConfigKey();
            if (configMap.containsKey(configKey)) {
                // 如果包含"site.browser.favicon"，修改对应的文件为使用
                if (configKey.contains("site.browser.favicon")) {
                    // 处理favicon文件更新
                    faviconFileHandlingStrategy.handleFaviconUpdate(
                            config.getConfigValue(),
                            configMap.get(configKey)
                    );
                }
                config.setConfigValue(configMap.get(configKey));
            }
        }
        this.updateBatchById(configs);
    }
}