package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 9:17
 */
@Data
public class ProjectClassification {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long parentId;
}
