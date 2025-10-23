package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 报告结果类型
 *
 * @TableName result_type
 */
@Data
@ToString
@TableName(value = "result_type")
public class ResultType implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Integer id;
    /**
     *
     */
    private String text;
    /**
     *
     */
    private String type;

}