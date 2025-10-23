package com.oriseq.dtm.vo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;


@Data
public class FileDownloadRequestVO {
    /**
     * 文件id集合
     */
    @NotEmpty
    private List<String> fileIds;
}