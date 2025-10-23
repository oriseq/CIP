package com.oriseq.dtm.vo.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author Hacah
 * @description: 修改权限
 */
@Data
@ToString
@NoArgsConstructor
public class PermissionUserVO {

    /**
     * userId
     */
    @NotNull
    private Long id;

    @NotNull
    private List<Long> permissionIds;

}
