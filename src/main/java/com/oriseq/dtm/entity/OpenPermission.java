package com.oriseq.dtm.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("open_permissions")
public class OpenPermission {
    private Long id;
    private Long userId;
    private Long permissionId;

}