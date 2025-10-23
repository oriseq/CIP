package com.oriseq.service;

import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectItemizedSpendDTO;
import com.oriseq.dtm.vo.ItemizedBillStatisticsVO;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/17 10:00
 */

public interface ItemizedBillStatisticsService {
    List<ProjectItemizedSpendDTO> staticProjectItemizedSpend(ItemizedBillStatisticsVO vo, LoginUser userInfo);
}
