package com.oriseq.controller.exception;


import com.oriseq.controller.utils.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String BAD_REQUEST_MSG = "客户端请求参数错误";

    // <1> 处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result<List<String>> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() + ":" + o.getDefaultMessage())
                .collect(Collectors.toList());
        Result<List<String>> listResult = new Result<>(false, HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
        return listResult;
    }

    // <2> 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<List<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() + ":" + o.getDefaultMessage())
                .collect(Collectors.toList());
        Result<List<String>> listResult = new Result<>(false, HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
        return listResult;
    }

    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result<List<String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        Result<List<String>> listResult = new Result<>(false, HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
        return listResult;
    }


    /**
     * token过期
     */
    @ExceptionHandler(value = TokenExpiresException.class)
    @ResponseBody
    public Result bizExceptionHandler(TokenExpiresException e) {
        Result<Object> result = new Result<>();
        result.setSuccess(false);
        result.setCode(e.getErrorCode());
        result.setMessage(e.getErrorMsg());
        result.setResult(null);
        return result;
    }

    /**
     * 没有权限
     */
    @ExceptionHandler(value = NoPermissionException.class)
    @ResponseBody
    public Result NoPermissionExceptionHandler(NoPermissionException e) {
        Result<Object> result = new Result<>();
        result.setSuccess(false);
        result.setCode(e.getErrorCode());
        result.setMessage(e.getErrorMsg());
        result.setResult(null);
        return result;
    }


    /**
     * 处理HttpRequestMethodNotSupportedException异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result runtimeExceptionHandler(HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        log.error(e.getMessage() + ":" + e.getStackTrace());
        Result result = new Result();
        result.setMessage(e.getMessage());
        result.setCode(ErrorEnum.INTERNAL_SERVER_ERROR.getErrorCode());
        result.setSuccess(false);
        result.setResult(null);
        return result;
    }

    /**
     * 处理RuntimeException异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        log.error(e.getMessage() + ":" + e.getStackTrace());
        Result result = new Result();
        result.setMessage(e.getMessage());
        result.setCode(ErrorEnum.INTERNAL_SERVER_ERROR.getErrorCode());
        result.setSuccess(false);
        result.setResult(null);
        return result;
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage() + ":" + e.getStackTrace());
        Result result = new Result();
        result.setMessage(ErrorEnum.INTERNAL_SERVER_ERROR.getErrorMsg());
        result.setCode(ErrorEnum.INTERNAL_SERVER_ERROR.getErrorCode());
        result.setSuccess(false);
        result.setResult(null);
        return result;
    }
}
