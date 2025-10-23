package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriseq.dtm.dto.NeedAlarmSampleProjectDTO;
import com.oriseq.dtm.dto.ProjectManageDTO;
import com.oriseq.dtm.entity.SampleProject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hacah
 * @className: SampleProjectMapper
 * @date 2024/5/6 15:47
 */
public interface SampleProjectMapper extends BaseMapper<SampleProject> {

    /**
     * 查询项目
     *
     * @param objectQueryWrapper
     */
    List<ProjectManageDTO> selectProjects(@Param("ew") QueryWrapper<Object> objectQueryWrapper);

    List<NeedAlarmSampleProjectDTO> selectNeedAlarmSampleProject();
}
