package com.oriseq.dtm.entity;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @TableName msg_group
 */
@Data
@ToString
@TableName(value = "msg_group", autoResultMap = true)
public class MsgGroup implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 接收通知的用户ids
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONArray userIds;
    /**
     * 接收组标识
     */
    @Length(max = 100, message = "编码长度不能超过100")
    private String groupIdentify;
    /**
     * name
     */
    private String name;
    /**
     * helpMsg
     */
    private String helpMsg;

}