package com.oriseq.dtm.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志实体
 *
 * @TableName log
 */
@Data
@TableName(value = "log")
public class Log implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private DateTime logTime;
    /**
     *
     */
    private String className;
    /**
     *
     */
    private String method;
    /**
     *
     */
    private String message;
    /**
     *
     */
    private String userId;
    /**
     *
     */
    private String ip;
    /**
     *
     */
    private String requestUrl;
    /**
     *
     */
    private String requestMethod;
    /**
     *
     */
    private String requestParams;
    /**
     * 响应的代码
     */
    private Integer responseStatus;
    /**
     *
     */
    private String exception;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Log other = (Log) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getLogTime() == null ? other.getLogTime() == null : this.getLogTime().equals(other.getLogTime()))
                && (this.getClassName() == null ? other.getClassName() == null : this.getClassName().equals(other.getClassName()))
                && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
                && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
                && (this.getRequestUrl() == null ? other.getRequestUrl() == null : this.getRequestUrl().equals(other.getRequestUrl()))
                && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
                && (this.getRequestParams() == null ? other.getRequestParams() == null : this.getRequestParams().equals(other.getRequestParams()))
                && (this.getResponseStatus() == null ? other.getResponseStatus() == null : this.getResponseStatus().equals(other.getResponseStatus()))
                && (this.getException() == null ? other.getException() == null : this.getException().equals(other.getException()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLogTime() == null) ? 0 : getLogTime().hashCode());
        result = prime * result + ((getClassName() == null) ? 0 : getClassName().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getRequestUrl() == null) ? 0 : getRequestUrl().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getRequestParams() == null) ? 0 : getRequestParams().hashCode());
        result = prime * result + ((getResponseStatus() == null) ? 0 : getResponseStatus().hashCode());
        result = prime * result + ((getException() == null) ? 0 : getException().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", logTime=").append(logTime);
        sb.append(", className=").append(className);
        sb.append(", method=").append(method);
        sb.append(", message=").append(message);
        sb.append(", userId=").append(userId);
        sb.append(", ip=").append(ip);
        sb.append(", requestUrl=").append(requestUrl);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", requestParams=").append(requestParams);
        sb.append(", responseStatus=").append(responseStatus);
        sb.append(", exception=").append(exception);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}