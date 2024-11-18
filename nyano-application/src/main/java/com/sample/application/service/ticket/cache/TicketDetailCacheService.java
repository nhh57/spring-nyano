package com.sample.application.service.ticket.cache;

import com.sample.domain.entity.TicketDetail;
import com.sample.domain.service.TicketDetailDomainService;
import com.sample.infrastructure.cache.redissss.RedisInfrasService;
import com.sample.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.sample.infrastructure.distributed.redisson.RedisDistributedService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailCacheService {
    @Autowired
    private RedisInfrasService redisInfrasService;
    @Autowired
    private RedisDistributedService redisDistributedService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    public TicketDetail getTicketDefaultCacheVip(Long id, Long version) {

        log.info("Impl getEventItemCacheVip->,{}, {}", id, version);
        TicketDetail ticketDetail = redisInfrasService.getObject(getEventItemKey(id), TicketDetail.class);
        if (ticketDetail != null) {
            return ticketDetail;
        }

        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_TICKET_KEY_ITEM" + id);
        try {
            // 1 tạo Lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unlock , bằng mọi giá
            // Lưu ý: Cho dù thành công hay không cũng phải unlock , bằng mọi giá
            // Lưu ý: Cho dù thành công hay không cũng phải unlock , bằng mọi giá

            if (!isLock) {
                return ticketDetail;
            }
            ticketDetail = redisInfrasService.getObject(getEventItemKey(id), TicketDetail.class);
            if (ticketDetail != null) {
                return ticketDetail;
            }
            // 3 -> vẫn không có thì truy vấn dbs
            ticketDetail = ticketDetailDomainService.getTicketDetailByID(id);
            log.info("FROM DBS->>> {}, {}", ticketDetail, version);
            if (ticketDetail != null) {
                log.info("TICKET NOT EXISTS...{}", version);
                // set
                redisInfrasService.setObject(getEventItemKey(id), ticketDetail);
                return ticketDetail;
            }

            // neu co thi set redis
            redisInfrasService.setObject(getEventItemKey(id), ticketDetail); // TTL 
            return ticketDetail;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Lưu ý: Cho dù thành công hay không cũ phải unlock, bằng mọi giá
            // Lưu ý: Cho dù thành công hay không cũ phải unlock, bằng mọi giá
            // Lưu ý: Cho dù thành công hay không cũ phải unlock, bằng mọi giá

            locker.unlock();
        }
    }


    private String getEventItemKey(Long item) {
        return "PRO_TICKET:ITEM:" + item;
    }
}
