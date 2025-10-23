package com.oriseq.dtm.entity;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "permissions", autoResultMap = true)
public class Permission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String permissionName;
    private String permissionIdentifier;
    private Long parentId;
    private String type;
    private String route;
    //    private String imageUrl;
    private Integer orderNo;
    private Integer level;
    /**
     * 是否启用（只对菜单生效，1：启用，0：不启用）
     */
    private Boolean enable;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject meta;

    @TableField(exist = false)
    private List<Permission> children;

    private String MenuNamePath;

    // 构造方法
    public Permission() {
    }

}