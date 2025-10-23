package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriseq.dtm.dto.ProjectDTO;
import com.oriseq.dtm.dto.innerProject.InnerProjectSearchDTO;
import com.oriseq.dtm.entity.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hacah
 * @className: ProjectMapper
 * @date 2024/5/6 15:47
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 根据用户组id查询项目
     * isSuper和 isInternalGroup查询所有
     *
     * @param isAll
     * @param userGroupId
     */
    List<ProjectDTO> selectAllProjectsByUserGroupId(Integer isAll, Long userGroupId);

    /**
     * 根据用户组id查询项目ids
     *
     * @param userGroupId
     */
    List<String> selectAllProjectIdsByUserGroupId(@Param("userGroupId") Long userGroupId);

    /**
     * 根据用户组id查询项目
     */
    List<Project> selectListByGroupId(@Param("userGroupId") Long userGroupId);

    List<InnerProjectSearchDTO> selectExtendList(@Param("ew") QueryWrapper<Object> queryWrapper);

    /**
     * 所有项目
     *
     * @return
     */
    List<ProjectDTO> selectAllProjects();
}
