package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Package {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String packageInfo;
    private Long userGroupId;
    private String projectIdList;
    private Long userId;
    private String remarks;

    public Package() {
    }
}