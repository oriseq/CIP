package com.oriseq.config.rateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisRateLimiter {

    private static final String RATE_LIMIT_PREFIX = "rate_limit";
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 限制访问
     *
     * @param suffix
     * @param limit
     * @param duration
     * @return
     */
    public boolean acquireLimit(String suffix, int limit, int duration) {
//        + "_"+index+":"
        String key = RATE_LIMIT_PREFIX + suffix;
        long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            // 设置 key 的过期时间为 1 分钟
            redisTemplate.expire(key, Duration.ofSeconds(duration));
        }
        // 如果当前访问次数超过限制次数,返回 false 表示限流
        return count <= limit;
    }

}