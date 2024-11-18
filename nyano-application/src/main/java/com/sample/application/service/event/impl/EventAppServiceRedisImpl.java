package com.sample.application.service.event.impl;

import com.sample.application.service.event.EventAppServiceRedis;
import com.sample.infrastructure.cache.redissss.RedisInfrasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class EventAppServiceRedisImpl implements EventAppServiceRedis {
    @Autowired
    private RedisInfrasService redisInfrasService;


    @Override
    public void saveSayHi(String who) {
        String key = "hainh";
        redisInfrasService.put(key, who,30L);
    }

    @Override
    public String getSayHiRedis(String key) {
        return redisInfrasService.getString(key);
    }
}
