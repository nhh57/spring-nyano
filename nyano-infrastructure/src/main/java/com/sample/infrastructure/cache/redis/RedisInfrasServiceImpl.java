package com.sample.infrastructure.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.infrastructure.cache.redissss.RedisInfrasService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisInfrasServiceImpl implements RedisInfrasService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(String key, String value) {
        if (StringUtils.hasLength(key)) { // null or ''
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf).orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        log.info("Set redis :: 1 {}", key);
        if (StringUtils.hasLength(key)) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("setObject error::{}", e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        Object result = redisTemplate.opsForValue().get(key);
        log.info("get Cache::{}", result);
        if (result == null) return null;
        // Nếu kết quả là 1 LinkedHashMap
        ObjectMapper mapper = new ObjectMapper();
        if (result instanceof Map) {
            try {
                // Chuyển đổi LinkedHashMap thành đối tượng mục tiêu
                return mapper.convertValue(result.toString(), clazz);
            } catch (IllegalArgumentException e) {
                log.error("Error converting LinkedHashMap to object ::{}", e.getMessage());
            }
        }
        // Nếu result là String thực hiện chuyển đổi bình thường
        if (result instanceof String) {
            try {
                return mapper.readValue((String) result, clazz);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing JSON to object ::{}", e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    public void put(String key, Object value, Long exp) {
        log.info("Put redis :: 1 {},{},{}", key, value, exp);
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
    public void put(String key, Object value, Long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
    }

}