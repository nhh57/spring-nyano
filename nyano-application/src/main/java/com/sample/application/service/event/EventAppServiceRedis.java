package com.sample.application.service.event;

public interface EventAppServiceRedis {
    void saveSayHi(String who);

    String getSayHiRedis(String key);
}
