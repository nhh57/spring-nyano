package com.sample.infrastructure.distributed.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.1.103:6319")
                .setDatabase(0);
        config.setCodec(new JsonJacksonCodec());  // Dùng codec Jackson thay vì MarshallingCodec
        return Redisson.create(config);
    }
}
