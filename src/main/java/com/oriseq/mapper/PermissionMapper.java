package com.oriseq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oriseq.dtm.entity.Permission;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hacah
 * @className: PermissionMapper
 * @date 2024/5/6 15:36
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 判断用户是否拥有该权限
     * <p>
     * *  if 用户组管理员
     * *   level in (2, 3)
     * *  if 普通用户：
     * *    level = 3
     * *
     *
     * @param permissionCode
     * @param userId
     * @return
     */
    Integer hasPermission(@Param("permissionCode") String permissionCode,
                          @Param("userId") String userId);
}
