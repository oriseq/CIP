package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.oriseq.dtm.dto.GroupUsersCascaderDTO;
import com.oriseq.dtm.dto.UserDTO;
import com.oriseq.dtm.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersMapper extends MPJBaseMapper<User> {


    /**
     * 用户信息
     *
     * @param userLambdaQueryWrapper
     * @return {@link List}<{@link UserDTO}>
     */
    List<UserDTO> selectUserInfo(@Param("ew") LambdaQueryWrapper<User> userLambdaQueryWrapper);

    /**
     * 用户信息
     *
     * @return
     */
    List<GroupUsersCascaderDTO> selectGroupUsersCascader();

}
