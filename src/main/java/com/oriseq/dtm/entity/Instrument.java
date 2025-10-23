package com.oriseq.dtm.entity;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @TableName instrument
 */
@TableName(value = "instrument", autoResultMap = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Instrument implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     *
     */
    private String remarks;
    /**
     * 导出字段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray exportFields;

    public Instrument(long id, String name) {
        this.id = id;
        this.name = name;
    }

}