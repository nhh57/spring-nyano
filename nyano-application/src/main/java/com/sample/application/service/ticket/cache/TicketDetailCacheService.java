package com.sample.application.service.ticket.cache;

import com.sample.domain.model.TicketDetail;
import com.sample.domain.service.TicketDetailDomainService;
import com.sample.infrastructure.cache.redissss.RedisInfrasService;
import com.sample.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.sample.infrastructure.distributed.redisson.RedisDistributedService;
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
            log.info("FROM CACHE EXIST {}", ticketDetail);
            return ticketDetail;
        }
        log.info("CACHE NO EXIST, START GET DB AND SET CACHE ->, {}, {}", id, version);
        // tao lock process voi key
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_TICKET_KEY_ITEM" + id);
        try {
            // 1 tạo Lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unlock , bằng mọi giá
            // Lưu ý: Cho dù thành công hay không cũng phải unlock , bằng mọi giá
            // Lưu ý: Cho dù thành công hay không cũng phải unlock , bằng mọi giá

            if (!isLock) {
                log.info("CACHE WAIT ITEM PLEASE ...{}", version);
                return ticketDetail;
            }
            // GET cache
            log.info("GET CACHE");
            ticketDetail = redisInfrasService.getObject(getEventItemKey(id), TicketDetail.class);
            //2. YES
            if (ticketDetail != null) {
                log.info("FROM CACHE EXIST {}, {}, {}", id, version, ticketDetail);
                return ticketDetail;
            }
            // 3 -> vẫn không có thì truy vấn dbs
            ticketDetail = ticketDetailDomainService.getTicketDetailByID(id);
            log.info("FROM DBS->>> {}, {}", ticketDetail, version);
            if (ticketDetail == null) { // Neu trong dbs van khong co thif return ve not exits;
                log.info("TICKET NOT EXISTS...{}", version);
                // set
                redisInfrasService.put(getEventItemKey(id), ticketDetail,5L); // TTL
                return ticketDetail;
            }

            // neu co thi set redis
            redisInfrasService.setObject(getEventItemKey(id), ticketDetail);
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
