package com.oriseq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.entity.Log;

/**
 * @author huang
 * @description 针对表【log】的数据库操作Service
 * @createDate 2025-01-15 14:30:51
 */
public interface LogService extends IService<Log> {

    public void saveLog(Log log);

}
