package com.sample.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sample.domain.model.TicketDetail;
import com.sample.domain.service.TicketDetailDomainService;
import com.sample.infrastructure.cache.redis.RedisInfrasService;
import com.sample.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.sample.infrastructure.distributed.redisson.RedisDistributedService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j

public class TicketDetailCacheService {
    @Autowired
    private RedisDistributedService redisDistributedService;
    @Autowired // Khai bao cache
    private RedisInfrasService redisInfrasService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;
    @Autowired
    private RedissonClient redissonClient;
    private static final Cache<Long, TicketDetail> ticketDetailLocalCache = CacheBuilder.newBuilder().initialCapacity(10) // 10 item trong cache
            .concurrencyLevel(7) // cpu
            .expireAfterWrite(1, TimeUnit.MINUTES) // thoi gian het han cuoi cung
            .build();

    private static final String CACHE_INVALIDATION_TOPIC = "ticket_detail_cache_invalidation";

    @Value("${spring.application.name}")
    private String intances;
//    public TicketDetail getTicketDefaultCacheNormal(Long id, Long version) {
//        // 1. get ticket item by redis
//        TicketDetail ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
//        // 2. YES -> Hit cache
//        if (ticketDetail != null) {
////            log.info("FROM CACHE {}, {}, {}", id, version, ticketDetail);
//            return ticketDetail;
//        }
//        // 3. If NO --> Missing cache
//
//        // 4. Get data from DBS
//        ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
////        log.info("FROM DBS {}, {}, {}", id, version, ticketDetail);
//
//        // 5. check ticketitem
//        if (ticketDetail != null) { // Nói sau khi code xong: Code nay co van de -> Gia su ticketItem lay ra tu dbs null thi sao, query mãi
//            // 6. set cache
//            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
//        }
//        return ticketDetail;
//    }

    // CHƯA VIP LẮM - KHI HỌ REVIEW CODE - SẼ BẮT VIẾT LẠI
//    public TicketDetail getTicketDefaultCacheVip(Long id, Long version) {
////        TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
//        TicketDetail ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
//        // show log: cache item
////        log.info("CACHE {}, {}, {}", id, version, ticketDetail);
//        // 2. YES
//        if (ticketDetail != null) {
//            log.info("FROM CACHE EXIST {}", ticketDetail);
//            return ticketDetail;
//        }
////        log.info("CACHE NO EXIST, START GET DB AND SET CACHE->, {}, {} ", id, version);
//        // Tao lock process voi KEY
//        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM" + id);
//        try {
//            // 1 - Tao lock
//            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
//            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
//            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
//            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
//            if (!isLock) {
////                log.info("LOCK WAIT ITEM PLEASE....{}", version);
//                return ticketDetail;
//            }
//            // stub...
//            // Get cache
//            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
//            // 2. YES
//            if (ticketDetail != null) {
////                log.info("FROM CACHE NGON A {}, {}, {}", id, version, ticketDetail);
//                return ticketDetail;
//            }
//            // 3 -> van khong co thi truy van DB
//
//            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
////            log.info("FROM DBS ->>>> {}, {}", ticketDetail, version);
//            if (ticketDetail == null) { // Neu trong dbs van khong co thi return ve not exists;

    /// /                log.info("TICKET NOT EXITS....{}", version);
//                // set
//                redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
//                return ticketDetail;
//            }
//
//            // neu co thi set redis
//            redisInfrasService.setObject(genEventItemKey(id), ticketDetail); // TTL
//            // set luon local
//            return ticketDetail;
//
//            // OK XONG, chung ta review code nay ok ... ddau vaof DDD thoi nao
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
//            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
//            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
//            locker.unlock();
//        }
//    }
    private TicketDetail getTicketDetailLocalCache(Long id) {
        try {
            return ticketDetailLocalCache.getIfPresent(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // CHƯA VIP LẮM - KHI HỌ REVIEW CODE - SẼ BẮT VIẾT LẠI
    public TicketDetail getTicketDefaultCacheLocal(Long id, Long version) {
        TicketDetail ticketDetail = getTicketDetailLocalCache(id);
        if (ticketDetail != null) {
            log.info("FROM LOCAL CACHE EXIST {}", ticketDetail);
            return ticketDetail;
        }
        ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
//        log.info("CACHE {}, {}, {}", id, version, ticketDetail);
        // 2. YES
        if (ticketDetail != null) {
            log.info("FROM DISTRIBUTED CACHE EXIST {}", ticketDetail);
            ticketDetailLocalCache.put(id, ticketDetail); // set item to local cache
            return ticketDetail;
        }

        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM" + id);
        try {
            // 1 - Tao lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if (!isLock) {
//                log.info("LOCK WAIT ITEM PLEASE....{}", version);
                return ticketDetail;
            }
            // stub...
            // Get cache
            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
            // 2. YES
            if (ticketDetail != null) {
//                log.info("FROM CACHE NGON A {}, {}, {}", id, version, ticketDetail);
                ticketDetailLocalCache.put(id, ticketDetail); // set item to local cache
                return ticketDetail;
            }
            // 3 -> van khong co thi truy van DB

            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
//            log.info("FROM DBS ->>>> {}, {}", ticketDetail, version);
            if (ticketDetail == null) { // Neu trong dbs van khong co thi return ve not exists;
//                log.info("TICKET NOT EXITS....{}", version);
                // set
                redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
                ticketDetailLocalCache.put(id, null); // set item to local cache
                log.info("FROM DBS NOT EXIST {}", ticketDetail);
                updateCache(id,version);
                return ticketDetail;
            }

            // neu co thi set redis
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail); // TTL
            ticketDetailLocalCache.put(id, ticketDetail); // set item to local cache
            log.info("FROM DBS CACHE EXIST {}", ticketDetail);
            updateCache(id,version);
            // set luon local
            return ticketDetail;

            // OK XONG, chung ta review code nay ok ... ddau vaof DDD thoi nao
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            locker.unlock();
        }
    }

    /**
     * Lắng nghe sự kiện invalidate từ Redis Pub/Sub
     */
    @PostConstruct
    public void subscribeToCacheInvalidation() {
        RTopic topic = redissonClient.getTopic(CACHE_INVALIDATION_TOPIC);
        topic.addListener(String.class, (channel, message) -> {
            // Phân tích thông điệp nhận được từ Redis Pub/Sub
            String[] parts = message.split(":");
            if (parts.length != 2) {
                log.warn("Invalid cache invalidation message format: {}", message);
                return;
            }

            String senderIdentifier = parts[0];  // Identifier của instance phát sự kiện
            log.info("Processing cache invalidation event for ticket ID: {}", senderIdentifier);
            Long ticketId = Long.parseLong(parts[1]);  // ID của ticket bị invalidate

            // Kiểm tra nếu event đến từ chính instance
            if (senderIdentifier.equals(intances)) {
                log.info("Ignoring cache invalidation event for ticket ID: {} from self instance: {}", ticketId, senderIdentifier);
                return;
            }

            // Nếu không phải từ chính mình, thực hiện invalidate local cache
            log.info("Processing cache invalidation event for ticket ID: {} from instance: {}", ticketId, senderIdentifier);
            invalidateLocalCache(ticketId);
        });

        log.info("Subscribed to Redis Pub/Sub channel: {}", CACHE_INVALIDATION_TOPIC);
    }

    /**
     * Invalidate cache local
     */
    private void invalidateLocalCache(Long id) {
        // Lấy dữ liệu từ cache trước khi invalid
        TicketDetail ticketDetail = getTicketDetailLocalCache(id);
        log.info("invalidateLocalCache LOCAL CACHE EXIST 1 {}", ticketDetail);

        // Xóa dữ liệu trong cache
        ticketDetailLocalCache.invalidate(id);
        log.info("Local cache invalidated for ticket ID: {}", id);

        // Kiểm tra lại cache sau khi invalid
        TicketDetail afterInvalidation = getTicketDetailLocalCache(id);
        log.info("invalidateLocalCache LOCAL CACHE EXIST 2 {}", afterInvalidation);
    }

//    /**
//     * Xóa cache local và Redis, đồng thời publish sự kiện invalidate
//     */
//    public void invalidateCache(Long id) {
//        // Xóa cache local
//        invalidateLocalCache(id);
//
//        // Xóa cache Redis
//        redisInfrasService.delete(genEventItemKey(id));
//
//        // Phát sự kiện invalidate lên Redis Pub/Sub
//        RTopic topic = redissonClient.getTopic(CACHE_INVALIDATION_TOPIC);
//        topic.publish(id);
//
//        log.info("Cache invalidation event published for ticket ID: {}", id);
//    }

    /**
     * Cập nhật dữ liệu cache local và Redis, sau đó publish sự kiện
     */

    public void updateCache(Long id, Long version) {
        // Lấy phiên bản của instance (ví dụ như UUID, Instance ID, hoặc Redis Node ID)


        // Log thông tin phiên bản và ID của ticket
        log.info("[{}] Update cache for ticket ID: {}", intances, id);
        String message = intances + ":" + id;
        // Phát sự kiện invalidate lên Redis Pub/Sub
        RTopic topic = redissonClient.getTopic(CACHE_INVALIDATION_TOPIC);
        topic.publish(message);  // Gửi ID của ticket vào Redis để thông báo invalidate cache

        // Log thông báo đã phát sự kiện
        log.info("[{}] Cache update event published for ticket ID: {}", intances, id);
    }


//    public String getInstanceVersion(Long version) {
//        String instanceId = intances;  // Ví dụ: lấy từ môi trường hoặc cấu hình
//        return instanceId + "_" + version;
//    }

    private String genEventItemKey(Long itemId) {
        return "PRO_TICKET:ITEM:" + itemId;
    }
}
