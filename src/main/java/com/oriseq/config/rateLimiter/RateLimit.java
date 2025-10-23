package com.oriseq.config.rateLimiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /**
     * 默认限制次数
     *
     * @return
     */
    int limit() default 100;

    /**
     * 默认限制时间（秒）
     *
     * @return
     */
    int duration() default 60;
}

