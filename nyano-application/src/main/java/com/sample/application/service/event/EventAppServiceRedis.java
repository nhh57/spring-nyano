package com.sample.application.service.event;

public interface EventAppServiceRedis {
    void saveSayHi(String who);

    Object getSayHiRedis(Object key);
}
