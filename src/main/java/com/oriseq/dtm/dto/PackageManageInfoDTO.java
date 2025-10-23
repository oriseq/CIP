package com.oriseq.dtm.dto;

import lombok.Data;

import java.util.List;

@Data
public class PackageManageInfoDTO {
    private Long id;
    private String packageName;
    private String userGroupName;
    private String projectIdList;
    private Long userGroupId;
    private String remarks;
    private List<Project> projects;


    public PackageManageInfoDTO() {
    }

    @Data
    public static class Project {
        private Long id;
        private String projectName;

    }

}