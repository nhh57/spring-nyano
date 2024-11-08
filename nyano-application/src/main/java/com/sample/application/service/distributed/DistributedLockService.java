package com.sample.application.service.distributed;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public interface DistributedLockService {
    public boolean acquireLock(String lockKey, long leaseTime, TimeUnit unit) ;

    public void releaseLock(String lockKey);
}
