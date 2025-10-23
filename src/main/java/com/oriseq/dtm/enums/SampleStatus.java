package com.oriseq.dtm.enums;

/**
 * Description: 样本状态
 *
 * @author hacah
 * @version 1.0
 * @date 2024/7/8 10:16
 */
public enum SampleStatus {
    /*
    * No project:0
NO_CONFIRMED_PROJECT:1
Waiting for testing:2
Testing in progress:3
Partially completed:4
Fully completed:5

样本状态
 无项目:0
无确认项目:1
等待检测:2
正在检测:3
部分完成:4
全部完成:5
    * */

    NO_PROJECT(0, "无项目"),
    NO_CONFIRMED_PROJECT(1, "无确认项目"),
    WAITING_FOR_TESTING(2, "等待检测"),
    TESTING_IN_PROGRESS(3, "正在检测"),
    PARTIALLY_COMPLETED(4, "部分完成"),
    FULLY_COMPLETED(5, "全部完成");


    private final int value;
    private final String name;

    SampleStatus(int value, String name) {
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
