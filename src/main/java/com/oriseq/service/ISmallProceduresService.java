package com.oriseq.service;

import com.oriseq.dtm.vo.LoginUserVO;

import java.util.Map;

/**
 * 小程序
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 13:36
 */
public interface ISmallProceduresService {
    /**
     * 设置用户信息
     *
     * @param rsp
     * @return
     */
    LoginUserVO setUserInfo(Map rsp);
}
