package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriseq.dtm.dto.ProjectListDTO;
import com.oriseq.dtm.dto.ProjectPriceDTO;
import com.oriseq.dtm.dto.ProjectStatisticsDTO;
import com.oriseq.dtm.entity.ProjectReferences;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huang
 * @description 针对表【project_references(项目引用表)】的数据库操作Mapper
 * @createDate 2024-11-11 14:57:06
 * @Entity com.oriseq.dtm.entity.ProjectReferences
 */
public interface ProjectReferencesMapper extends BaseMapper<ProjectReferences> {

    List<ProjectListDTO> selectProjects(@Param("ew") QueryWrapper<Object> queryWrapper);

    ProjectPriceDTO getSUMProjectPrice(@Param("ew") QueryWrapper<Object> in, @Param("innew") QueryWrapper<Object> ined, @Param("userGroupId") Long userGroupId);

    List<ProjectStatisticsDTO> getProjectNumAndSpend(@Param("ew") QueryWrapper<Object> queryWrapper);
}




