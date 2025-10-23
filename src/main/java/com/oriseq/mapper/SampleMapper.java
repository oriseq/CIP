package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.oriseq.dtm.dto.ProjectItemizedSpendDTO;
import com.oriseq.dtm.dto.SampleDTO;
import com.oriseq.dtm.entity.Project;
import com.oriseq.dtm.entity.Sample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hacah
 * @className: SampleMapper
 * @date 2024/5/6 15:47
 */
public interface SampleMapper extends BaseMapper<Sample> {

    /**
     * 查询样本信息 by id
     *
     * @param id
     * @return
     */
    List<SampleDTO> selectInfoById(@Param("id") Long id);

    /**
     * @param page
     * @param wrapper
     * @param projectIds
     * @return
     */
    IPage<Sample> selectPageSamples(IPage<Sample> page, @Param("ew") Wrapper<Object> wrapper, @Param("projectIds") List<Long> projectIds);

    /**
     * @param sampleIds
     * @return
     */
    List<Project> selectProjectsBySampleIds(@Param("sampleIds") List<Long> sampleIds);

    /**
     * 明细项目账单统计
     *
     * @param wrapper
     * @return
     */
    List<ProjectItemizedSpendDTO> staticProjectItemizedSpend(@Param("ew") Wrapper<Object> wrapper);

}
