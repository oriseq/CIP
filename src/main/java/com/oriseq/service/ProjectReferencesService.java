package com.oriseq.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.ProjectListDTO;
import com.oriseq.dtm.dto.ProjectPriceDTO;
import com.oriseq.dtm.entity.ProjectReferences;
import com.oriseq.dtm.vo.billStatistics.BillStatisticsVO;
import com.oriseq.dtm.vo.project.ProjectSearchVO;

import java.util.List;

/**
 * @author huang
 * @description 针对表【project_references(项目引用表)】的数据库操作Service
 * @createDate 2024-11-11 14:57:06
 */
public interface ProjectReferencesService extends IService<ProjectReferences> {

    /**
     * 查询项目
     *
     * @return
     */
    List<ProjectListDTO> getProjects(LoginUser userInfo, ProjectSearchVO projectSearchVO);

    /**
     * 获取总价（项目价格求和）
     *
     * @param in
     * @param ined
     * @param userGroupId
     * @return
     */
    ProjectPriceDTO getSUMProjectPrice(QueryWrapper<Object> in, QueryWrapper<Object> ined, Long userGroupId);

    List<JSONObject> staticProjectNumAndSpend(BillStatisticsVO billStatisticsVO, LoginUser userInfo);
}
