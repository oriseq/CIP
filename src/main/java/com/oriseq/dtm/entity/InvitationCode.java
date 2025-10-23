package com.oriseq.dtm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("invitation_codes")
public class InvitationCode {
    @TableId(type = IdType.AUTO)
    private Long id; // ID（key）
    private Long userGroupId; // 用户组ID
    private String invitationCode; // 邀请码
    private LocalDateTime expiryDate; // 有效时间
    private LocalDateTime createTime; // 创建时间

    public InvitationCode(Long userGroupId, String invitationCode, LocalDateTime expiryDate, LocalDateTime createTime) {
        this.userGroupId = userGroupId;
        this.invitationCode = invitationCode;
        this.expiryDate = expiryDate;
        this.createTime = createTime;
    }
}