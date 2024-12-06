package com.sample.infrastructure.cache.redis;

import java.util.concurrent.TimeUnit;

public interface RedisInfrasService {
    void setString(String key, String value);

    String getString(String key);

    void setObject(String key, Object value);

    <T> T getObject(String key, Class<T> clazz);

     void put(String key, Object value, Long exp);

     void put(String key, Object value, Long ttl, TimeUnit timeUnit);
}
