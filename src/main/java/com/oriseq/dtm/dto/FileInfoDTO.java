package com.oriseq.dtm.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * FileInfo的DTO
 */
@Data
public class FileInfoDTO implements Serializable {
    private String id;
    private String originalFileName;
    private Long fileSize;
}
