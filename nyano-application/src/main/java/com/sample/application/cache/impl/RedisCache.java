package com.sample.application.cache.impl;


import com.sample.application.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisCache implements Cache {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    private RedissonClient redissonClient; // Inject RedissonClient

    public RedisCache() {
    }

    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(Object key, Object value, Long exp) {
        put(key, value, exp, TimeUnit.SECONDS);
    }

    /**
     * Lưu trữ giá trị vào Redis với thời hạn hiệu lực.
     *
     * @param key      Khóa của giá trị
     * @param value    Giá trị cần lưu trữ
     * @param ttl      Thời hạn hiệu lực
     * @param timeUnit Đơn vị thời gian cho thời hạn hiệu lực
     */
    @Override
    public void put(Object key, Object value, Long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
    }

    @Override
    public boolean hasKey(Object key) {
        return this.redisTemplate.opsForValue().get(key) != null;
    }

    @Override
    public Boolean remove(Object key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long increment(String key, long liveTime) {
        RedisAtomicLong atomicCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long counterValue = atomicCounter.getAndIncrement();
        //Thiết lập thời gian hết hạn ban đầu
        if (counterValue == 0 && liveTime > 0) {
            atomicCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return counterValue;
    }

    @Override
    public void batchDelete(Collection keys) {
        redisTemplate.delete(keys);
    }


    public boolean acquireLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // Thử lấy khóa, đợi trong thời gian chờ (waitTime), giữ khóa trong thời gian (leaseTime)
            return lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Failed to acquire lock", e);
            return false;
        }
    }

    public void releaseLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}