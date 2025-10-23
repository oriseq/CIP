package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置表
 *
 * @TableName sys_config
 */
@TableName(value = "sys_config")
@Data
public class SysConfig implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 配置键，使用点号分隔的多级名称，例如：login.basic.mainTitle
     */
    private String configKey;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 配置类型：string、boolean、number、json
     */
    private String valueType;
    /**
     * 配置描述
     */
    private String description;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}