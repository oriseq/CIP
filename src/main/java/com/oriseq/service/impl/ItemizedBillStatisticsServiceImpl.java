package com.oriseq.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectItemizedSpendDTO;
import com.oriseq.dtm.vo.ItemizedBillStatisticsVO;
import com.oriseq.mapper.SampleMapper;
import com.oriseq.service.ItemizedBillStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2025/4/17 10:01
 */
@Service
public class ItemizedBillStatisticsServiceImpl implements ItemizedBillStatisticsService {

    @Autowired
    private SampleMapper sampleMapper;

    @Override
    public List<ProjectItemizedSpendDTO> staticProjectItemizedSpend(ItemizedBillStatisticsVO vo, LoginUser userInfo) {
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        // 非内部组和超级管理员，只查询自己组本身数据
        if (!userInfo.isSuper() && !userInfo.getIsInternalGroup()) {
            queryWrapper.eq("s.sample_user_group_id", userInfo.getUserGroupId());
        }
        queryWrapper.eq(true, "s.is_del", 0);
        // 非内部组数据
        queryWrapper.eq(true, "ug.is_internal_group", 0);
        // 条件查询
        if (!Objects.isNull(vo)) {
            queryWrapper.in(CollUtil.isNotEmpty(vo.getProjectIds()), "sp.project_id", vo.getProjectIds());
            if (vo.getCreationTime() != null && vo.getCreationTime().size() == 2) {
                queryWrapper.ge("sp.creation_time", vo.getCreationTime().get(0));
                queryWrapper.le("sp.creation_time", vo.getCreationTime().get(1));
            }
            queryWrapper.eq(Objects.nonNull(vo.getUseGroupId()), "s.sample_user_group_id", vo.getUseGroupId());
        }
        queryWrapper.orderBy(true, false, "sp.creation_time");
        // 逻辑上，账单明细统计和账单汇总统计统计是一样的
        List<ProjectItemizedSpendDTO> projectItemizedSpendDTOS = sampleMapper.staticProjectItemizedSpend(queryWrapper);
        return projectItemizedSpendDTOS;
    }
}
