package com.oriseq.dtm.vo.sample;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FileInfoVO {
    private String id;
    private String originalFileName;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime creationTime;

    /**
     * 项目名称列表
     */
    private List<String> projectNames;
}
