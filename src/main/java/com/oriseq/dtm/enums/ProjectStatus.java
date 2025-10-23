package com.oriseq.dtm.enums;

public enum ProjectStatus {
    CANCELLED(0, "取消"),
    PENDING(1, "待定"),
    CONFIRMING(2, "确认检测"),
    IN_PROGRESS(3, "正在检测"),
    COMPLETED(4, "已完成");

    private final int value;
    private final String name;

    ProjectStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ProjectStatus fromValue(int value) {
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid project status value: " + value);
    }

    public static String getNameByValue(int value) {
        ProjectStatus status = fromValue(value);
        return status.getName();
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}