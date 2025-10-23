package com.oriseq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.entity.Log;
import com.oriseq.mapper.LogMapper;
import com.oriseq.service.LogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huang
 * @description 针对表【log】的数据库操作Service实现
 * @createDate 2025-01-15 14:30:51
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
        implements LogService {

    @Override
    @Async("asyncExecutor")
    public void saveLog(Log log) {
        baseMapper.insert(log);
    }
}




