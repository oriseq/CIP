package com.oriseq.controller.exception;

public class TokenExpiresException extends RuntimeException {

    protected Integer errorCode;
    protected String errorMsg;

    public TokenExpiresException() {

    }

    public TokenExpiresException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
