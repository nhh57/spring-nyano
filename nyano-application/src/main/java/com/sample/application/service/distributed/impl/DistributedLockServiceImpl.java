package com.sample.application.service.distributed.impl;

import com.sample.application.service.distributed.DistributedLockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service

public class DistributedLockServiceImpl implements DistributedLockService {
    private final RedissonClient redissonClient;

    public DistributedLockServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean acquireLock(String lockKey, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // Cố gắng lấy khóa, đặt thời gian lease (thời gian tự động giải phóng khóa)
            return lock.tryLock(0, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
