package com.oriseq.dtm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName msg_group
 */
@Data
@ToString
public class MsgUpdateGroupVO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Long id;
    /**
     * 接收通知的用户ids
     */
    private List<Long> userIds;

}