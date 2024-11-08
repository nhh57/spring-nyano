package com.sample.application.service.event.impl;

import com.sample.application.cache.impl.RedisCache;
import com.sample.application.service.event.EventAppServiceRedis;
import org.springframework.stereotype.Service;

@Service

public class EventAppServiceRedisImpl implements EventAppServiceRedis {
    private final RedisCache redisCache;

    public EventAppServiceRedisImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public void saveSayHi(String who) {
        Object key = "hainh";
        redisCache.put(key, who,30L);
    }

    @Override
    public Object getSayHiRedis(Object key) {
        return redisCache.get(key);
    }
}
