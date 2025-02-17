package com.example.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 */
@Slf4j
@Component
public class RedisComponent {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisComponent(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存储数据
     * @param key String
     * @param value Object
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存储数据并设置过期时间
     * @param key String
     * @param value Object
     * @param timeout long
     * @param unit TimeUnit
     */
    public void setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取数据
     * @param key String
     * @param classType Class<T>
     * @return <T>
     */
    public <T> T get(String key, Class<T> classType) {
        return classType.cast(redisTemplate.opsForValue().get(key));
    }

    /**
     * 删除数据
     * @param key String
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 检查 key 是否存在
     * @param key String
     * @return boolean
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置 key 的过期时间
     * @param key String
     * @param timeout long
     * @param unit TimeUnit
     * @return boolean
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取 key 的剩余过期时间
     * @param key String
     * @param unit TimeUnit
     * @return Long
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }
}
