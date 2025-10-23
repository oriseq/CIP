package com.oriseq.controller.exception;

public enum ErrorEnum {
    // 数据操作错误定义
    SUCCESS(200, "nice"),
    NO_PERMISSION(403, "没有权限"),
    NO_AUTH(401, "请先登录再访问"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "请联系管理员");

    /** 错误码 */
    private Integer errorCode;

    /** 错误信息 */
    private String errorMsg;

    ErrorEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
