package com.oriseq.controller.utils;

import lombok.Data;

@Data
public class Result<T> {
    //是否成功
    private Boolean success;
    //状态码
    private Integer code;
    /**
     * success
     */
//    private String type;
    //提示信息
    private String message;
    //数据
    private T result;

    public Result() {

    }

    //自定义返回结果的构造方法
    public Result(Boolean success, Integer code, String message, T result) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    /**
     * 返回默认错误,可设置消息
     *
     * @return
     */
    public static <T> Result<T> defaultErrorByMessage(String msg) {
        return new Result<>(false, -1, msg, null);
    }

    /**
     * 返回默认错误,可设置消息
     *
     * @return
     */
    public static <T> Result<T> defaultErrorByMessageAndData(String msg, T data) {
        return new Result<>(false, -1, msg, data);
    }

    /**
     * 返回默认成功，可设置消息
     *
     * @return
     */
    public static <T> Result<T> defaultSuccessByMessage(String msg) {
        return new Result<>(true, 0, msg, null);
    }

    /**
     * 返回默认错误,可设置消息
     *
     * @return
     */
    public static <T> Result<T> defaultSuccessByMessageAndData(String msg, T data) {
        return new Result<>(true, 0, msg, data);
    }

}
