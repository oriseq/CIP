package com.oriseq.dtm.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PackageGroupDTO {
    private Long userGroupId;
    private String groupName;
    private Long packageId;
    private String packageName;

}