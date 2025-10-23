package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.oriseq.dtm.entity.Permission;
import com.oriseq.dtm.entity.User;
import com.oriseq.dtm.entity.UserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Hacah
 * @className: UserGroupMapper
 * @date 2024/5/6 15:47
 */
public interface UserGroupMapper extends MPJBaseMapper<UserGroup> {

    UserGroup getUserGroupById(@Param("id") Long id);


    List<Permission> getGeneralUserPermissionByPhone(@Param("phone") String phone);


    /**
     * @return
     */
    List<Permission> getButtonPermission(@Param("ew") LambdaQueryWrapper<User> ew);

}
