package com.sample.infrastructure.cache.redissss;

public interface RedisInfrasService {
    void setString(String key, String value);

    String getString(String key);

    void setObject(String key, Object value);

    <T> T getObject(String key, Class<T> clazz);

}
