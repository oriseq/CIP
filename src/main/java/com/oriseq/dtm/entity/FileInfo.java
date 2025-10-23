package com.oriseq.dtm.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oriseq.common.utils.ImageUtils;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@TableName("file")
@ToString
public class FileInfo {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String originalFileName;
    private String path;
    private Long sampleId;
    /**
     * 附件类型：
     * 1：图片
     * 2：其他
     */
    private String attachmentType;
    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 是否使用，0：否，1：是  记录系统有没有用到这个文件
     */
    private Boolean isUse;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime creationTime;


    /**
     * 设置附件类型
     * 1：图片
     * 2：其他
     *
     * @return
     */
    public void setAttachmentType() {
        if (!StringUtils.hasText(this.getOriginalFileName())) {
            throw new RuntimeException("originalFileName为空");
        }
        try {
            boolean imageBySuffix = ImageUtils.isImageBySuffix(this.getOriginalFileName());
            if (imageBySuffix) {
                this.setAttachmentType("1");
            } else {
                this.setAttachmentType("2");
            }
        } catch (Exception e) {
            this.setAttachmentType("2");
        }
    }
}