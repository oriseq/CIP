package com.oriseq.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.gson.JsonObject;
import com.oriseq.dtm.dto.LoginUser;
import com.oriseq.dtm.dto.innerProject.InnerProjectSearchDTO;
import com.oriseq.dtm.entity.Project;

import java.util.Collection;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/21 14:16
 */
public interface IProjectService extends IService<Project> {

    /**
     * 通过用户组id获取所有分类项目列表
     *
     * @param userInfo
     * @return
     */
    Collection<JsonObject> getProjectsByUserGroup(LoginUser userInfo);

    /**
     * 通过用户组id获取一层分类项目列表
     *
     * @param userInfo
     * @return
     */
    Collection<JsonObject> getOneClassificationProjectsByUserGroup(LoginUser userInfo);


    /**
     * 获取可查看所有project
     *
     * @param userInfo
     * @return
     */
    List<Project> getProjects(LoginUser userInfo);

    List<InnerProjectSearchDTO> extendList(QueryWrapper<Object> queryWrapper);

    /**
     * 获取所有项目,按照分类
     *
     * @param userInfo
     * @return
     */
    Collection<JsonObject> getAllProjectsByUserGroup(LoginUser userInfo);

}
